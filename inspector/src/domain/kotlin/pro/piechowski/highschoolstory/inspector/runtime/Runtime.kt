package pro.piechowski.highschoolstory.inspector.runtime

import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.StateFlow

interface Runtime {
    val launchedGameJob: StateFlow<Job?>

    fun launchGame()
}
