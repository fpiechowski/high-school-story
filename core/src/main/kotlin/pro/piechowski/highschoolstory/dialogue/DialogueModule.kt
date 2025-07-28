package pro.piechowski.highschoolstory.dialogue

import org.koin.dsl.module

val DialogueModule =
    module {
        single { DialogueManager() }
        single { DialogueUserInterface() }
        single { DialogueUserInterfaceUpdater() }
        single { DialogueInputProcessor() }
    }
