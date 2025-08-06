package pro.piechowski.highschoolstory.scene

import org.koin.dsl.module

val SceneModule =
    module {
        single { IntroScene() }
    }
