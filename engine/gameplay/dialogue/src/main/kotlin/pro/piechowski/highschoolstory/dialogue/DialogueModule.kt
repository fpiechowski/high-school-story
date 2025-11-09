package pro.piechowski.highschoolstory.dialogue

import org.koin.dsl.module
import pro.piechowski.highschoolstory.input.dialogue.DialogueInputProcessor
import pro.piechowski.highschoolstory.ui.dialogue.DialogueUserInterface
import pro.piechowski.highschoolstory.ui.dialogue.DialogueUserInterfaceUpdater

val DialogueModule =
    module {
        single(createdAtStart = true) { DialogueManager() }
        single { DialogueUserInterface() }
        single(createdAtStart = true) { DialogueUserInterfaceUpdater() }
        single { DialogueInputProcessor() }
    }
