package pro.piechowski.highschoolstory.scene

import kotlinx.coroutines.Job

abstract class Scene {
    abstract suspend fun play(): Job
}
