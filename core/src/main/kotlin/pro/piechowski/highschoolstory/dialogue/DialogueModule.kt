package pro.piechowski.highschoolstory.dialogue

import org.koin.dsl.module
import pro.piechowski.highschoolstory.dialogue.input.DialogueInputProcessor
import pro.piechowski.highschoolstory.dialogue.ui.DialogueUserInterface
import pro.piechowski.highschoolstory.dialogue.ui.DialogueUserInterfaceUpdater

val DialogueModule =
    module {
        single(createdAtStart = true) { DialogueManager() }
        single { DialogueUserInterface() }
        single { DialogueUserInterfaceUpdater() }
        single { DialogueInputProcessor() }
    }
