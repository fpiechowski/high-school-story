package pro.piechowski.highschoolstory.interaction

import org.koin.dsl.module
import pro.piechowski.highschoolstory.input.interaction.InteractionInputProcessor
import pro.piechowski.highschoolstory.interaction.interactable.InteractableDebugSystem
import pro.piechowski.highschoolstory.interaction.interactor.InteractorDebugSystem

val InteractionModule =
    module {
        single { InteractionInputProcessor() }
        single { InteractionSystem() }
        single { InteractorDebugSystem() }
        single { InteractableDebugSystem() }
    }
