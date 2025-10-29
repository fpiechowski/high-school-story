package pro.piechowski.highschoolstory.inspector.runtime

import com.badlogic.gdx.Gdx
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import pro.piechowski.highschoolstory.inspector.classGraph
import kotlin.reflect.KFunction
import kotlin.reflect.jvm.kotlinFunction

@DelicateCoroutinesApi
@ExperimentalCoroutinesApi
class LibGDXRuntime : Runtime {
    override val gameScope: CoroutineScope = CoroutineScope(newSingleThreadContext("GameScope"))

    private val _launchedGameJob = MutableStateFlow<Job?>(null)
    override val launchedGameJob: StateFlow<Job?> = _launchedGameJob

    override val launcher: Runtime.Launcher
        get() =
            classGraph
                .scan()
                .use { scan ->
                    val launchRuntimeFunction: KFunction<*> =
                        scan
                            .getClassesWithMethodAnnotation(RuntimeLauncher::class.java)
                            .first()
                            .loadClass()
                            .methods
                            .find { method -> method.annotations.any { it.annotationClass == RuntimeLauncher::class } }
                            ?.kotlinFunction ?: throw RuntimeLauncherNotFoundException()

                    Runtime.Launcher {
                        launchRuntimeFunction.call()
                    }
                }

    override fun start() {
        launchedGameJob.value?.let {
            if (it.isActive) return
        }

        _launchedGameJob.value =
            gameScope
                .launch {
                    launcher.launch()
                }.also {
                    it.invokeOnCompletion {
                        _launchedGameJob.value = null
                    }
                }
    }

    override fun stop() {
        Gdx.app.exit()
    }

    override fun pause() {
        TODO("Not yet implemented")
    }
}
