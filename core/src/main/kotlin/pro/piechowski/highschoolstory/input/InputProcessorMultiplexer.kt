package pro.piechowski.highschoolstory.input

import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.scenes.scene2d.Stage
import io.github.oshai.kotlinlogging.KotlinLogging
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import pro.piechowski.highschoolstory.interaction.input.InteractionInputProcessor

class InputProcessorMultiplexer :
    InputMultiplexer(),
    KoinComponent {
    val interactionInputProcessor by inject<InteractionInputProcessor>()
    val stage by inject<Stage>()

    private val logger = KotlinLogging.logger { }

    init {
        addProcessor(interactionInputProcessor)
        addProcessor(stage)

        logger.debug { "Input multiplexer initialized" }
    }
}
