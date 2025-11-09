package pro.piechowski.highschoolstory.input.interaction

import com.github.quillraven.fleks.World
import io.github.oshai.kotlinlogging.KotlinLogging
import ktx.app.KtxInputAdapter
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import pro.piechowski.highschoolstory.input.InputAction
import pro.piechowski.highschoolstory.input.InputManager
import pro.piechowski.highschoolstory.input.InputMapping
import pro.piechowski.highschoolstory.interaction.interactor.Interactor

class InteractionInputProcessor :
    KtxInputAdapter,
    KoinComponent {
    private val world: World by inject()
    private val inputManager: InputManager by inject()
    private val inputMapping: InputMapping by inject()

    private val logger = KotlinLogging.logger { }

    override fun keyUp(keycode: Int): Boolean {
        val inputOwner = inputManager.owner.value

        if (inputOwner is Interactor) {
            if (with(inputMapping) { InputAction.Interaction.mapsToKey(keycode) }) {
                world
                    .family { all(InteractionInput) }
                    .forEach {
                        it[InteractionInput].interacting = true
                    }

                logger.debug { "Interaction key released" }

                return true
            }
        }

        return false
    }
}
