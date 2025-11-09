@file:JvmName("Lwjgl3Launcher")

package pro.piechowski.highschoolstory.lwjgl3

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.core.annotation.KoinInternalApi
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module
import pro.piechowski.highschoolstory.Config
import pro.piechowski.highschoolstory.Entrypoint
import pro.piechowski.highschoolstory.Main
import pro.piechowski.highschoolstory.coreModule
import pro.piechowski.highschoolstory.entrypointOverride

@ExperimentalCoroutinesApi
@ExperimentalContextParameters
object Launcher : KoinComponent {
    private val config: Config by inject()

    @KoinInternalApi
    fun launch(
        modules: List<Module> = emptyList(),
        entrypoint: Entrypoint? = null,
    ) {
        val koin =
            startKoin {
                modules(modules + coreModule())
            }.koin

        if (entrypointOverride == null) {
            entrypointOverride = entrypoint
        }

        entrypointOverride?.let { entrypoint ->
            koin.loadModules(listOf(module { single<Entrypoint> { entrypoint } }), allowOverride = true)
        }

        val applicationConfig = config.application

        if (StartupHelper.startNewJvmIfRequired()) {
            return
        }

        Lwjgl3Application(
            Main(koin),
            Lwjgl3ApplicationConfiguration().apply {
                setTitle(applicationConfig.title)
                useVsync(applicationConfig.useVsync)
                setForegroundFPS(Lwjgl3ApplicationConfiguration.getDisplayMode().refreshRate + 1)

                setWindowedMode(
                    applicationConfig.resolution.width,
                    applicationConfig.resolution.height,
                )

                setWindowIcon(*(arrayOf(128, 64, 32, 16).map { "libgdx$it.png" }.toTypedArray()))
            },
        )
    }
}
