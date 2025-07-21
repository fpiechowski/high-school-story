package pro.piechowski.highschoolstory.interaction.input

import com.badlogic.gdx.Input
import com.github.quillraven.fleks.World
import io.github.oshai.kotlinlogging.KotlinLogging
import ktx.app.KtxInputAdapter
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class InteractionInputProcessor :
    KtxInputAdapter,
    KoinComponent {
    private val world: World by inject()

    private val logger = KotlinLogging.logger { }

    override fun keyUp(keycode: Int): Boolean {
        if (keycode in INTERACTION_KEYS) {
            world
                .family { all(InteractionInput) }
                .forEach {
                    it[InteractionInput].interacting = true
                }

            logger.debug { "Interaction key released" }

            return true
        }

        return false
    }

    companion object {
        val INTERACTION_KEYS = listOf(Input.Keys.ENTER)
    }
}
