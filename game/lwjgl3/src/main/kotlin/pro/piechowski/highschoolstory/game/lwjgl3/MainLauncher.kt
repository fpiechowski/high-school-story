@file:JvmName("Lwjgl3Launcher")

package pro.piechowski.highschoolstory.game.lwjgl3

import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.core.annotation.KoinInternalApi
import pro.piechowski.highschoolstory.highSchoolStoryModule
import pro.piechowski.highschoolstory.lwjgl3.Launcher

@ExperimentalCoroutinesApi
@ExperimentalContextParameters
@KoinInternalApi
fun main() {
    Launcher.launch(
        listOf(
            highSchoolStoryModule,
        ),
    )
}
