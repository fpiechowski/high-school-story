package pro.piechowski.highschoolstory.input

import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.scenes.scene2d.Stage
import io.github.oshai.kotlinlogging.KotlinLogging
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import pro.piechowski.highschoolstory.Config
import pro.piechowski.highschoolstory.debug.camera.DebugCameraControlInputProcessor
import pro.piechowski.highschoolstory.debug.highlight.DebugEntityHighlightInputProcessor
import pro.piechowski.highschoolstory.debug.selection.DebugSelectionInputProcessor
import pro.piechowski.highschoolstory.dialogue.input.DialogueInputProcessor
import pro.piechowski.highschoolstory.interaction.input.InteractionInputProcessor

class GameInputMultiplexer :
    InputMultiplexer(),
    KoinComponent {
    val interactionInputProcessor by inject<InteractionInputProcessor>()
    val dialogueInputProcessor by inject<DialogueInputProcessor>()
    val debugSelectionInputProcessor by inject<DebugSelectionInputProcessor>()

    val debugEntityHighlightInputProcessor by inject<DebugEntityHighlightInputProcessor>()
    val debugCameraMovementInputProcessor by inject<DebugCameraControlInputProcessor>()
    val stage by inject<Stage>()
    val config by inject<Config>()

    private val logger = KotlinLogging.logger { }

    init {
        if (config.debug) {
            addProcessor(debugSelectionInputProcessor)
            addProcessor(debugEntityHighlightInputProcessor)
            addProcessor(debugCameraMovementInputProcessor)
        }

        addProcessor(stage)
        addProcessor(interactionInputProcessor)
        addProcessor(dialogueInputProcessor)

        logger.debug { "Input multiplexer initialized" }
    }
}
