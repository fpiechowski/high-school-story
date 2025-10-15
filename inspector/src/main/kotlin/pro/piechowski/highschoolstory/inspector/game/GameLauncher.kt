package pro.piechowski.highschoolstory.inspector.game

import kotlinx.coroutines.launch
import pro.piechowski.highschoolstory.lwjgl3.launchLwjgl3

class GameLauncher {
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
