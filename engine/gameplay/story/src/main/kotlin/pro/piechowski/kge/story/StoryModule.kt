package pro.piechowski.kge.story

import org.koin.dsl.module

val StoryModule =
    module {
        single { StoryManager() }
    }
