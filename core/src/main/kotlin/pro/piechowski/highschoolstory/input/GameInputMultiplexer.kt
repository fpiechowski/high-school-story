package pro.piechowski.highschoolstory.input

import com.badlogic.gdx.InputMultiplexer
import io.github.oshai.kotlinlogging.KotlinLogging
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import pro.piechowski.highschoolstory.interaction.input.InteractionInputProcessor

class GameInputMultiplexer :
    InputMultiplexer(),
    KoinComponent {
    val interactionInputProcessor by inject<InteractionInputProcessor>()

    private val logger = KotlinLogging.logger { }

    init {
        addProcessor(interactionInputProcessor)

        logger.debug { "Input multiplexer initialized" }
    }
}
