package pro.piechowski.highschoolstory.inspector.game

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import pro.piechowski.highschoolstory.lwjgl3.launchLwjgl3

class GameInspectorModel(
    private val gameScope: CoroutineScope,
) {
    private val _gameLaunchJob = MutableStateFlow<Job?>(null)
    val gameLaunchJob: StateFlow<Job?> = _gameLaunchJob

    fun launchGame() {
        gameLaunchJob.value?.let {
            if (it.isActive) return
        }

        _gameLaunchJob.value =
            gameScope
                .launch {
                    launchLwjgl3()
                }.also {
                    it.invokeOnCompletion {
                        _gameLaunchJob.value = null
                    }
                }
    }
}
