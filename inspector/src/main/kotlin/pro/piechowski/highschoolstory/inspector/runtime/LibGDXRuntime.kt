package pro.piechowski.highschoolstory.inspector.runtime

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import pro.piechowski.highschoolstory.lwjgl3.launchLwjgl3

@DelicateCoroutinesApi
@ExperimentalCoroutinesApi
class LibGDXRuntime(
    private val gameScope: CoroutineScope = CoroutineScope(newSingleThreadContext("GameScope")),
) : Runtime {
    private val _launchedGameJob = MutableStateFlow<Job?>(null)
    override val launchedGameJob: StateFlow<Job?> = _launchedGameJob

    override fun launchGame() {
        launchedGameJob.value?.let {
            if (it.isActive) return
        }

        _launchedGameJob.value =
            gameScope
                .launch {
                    launchLwjgl3()
                }.also {
                    it.invokeOnCompletion {
                        _launchedGameJob.value = null
                    }
                }
    }
}
