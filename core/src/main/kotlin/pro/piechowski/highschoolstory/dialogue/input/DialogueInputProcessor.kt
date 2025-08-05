package pro.piechowski.highschoolstory.dialogue.input

import com.badlogic.gdx.Input
import ktx.app.KtxInputAdapter
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import pro.piechowski.highschoolstory.dialogue.DialogueManager
import pro.piechowski.highschoolstory.input.InputState

class DialogueInputProcessor :
    KtxInputAdapter,
    KoinComponent {
    private val dialogueManager: DialogueManager by inject()
    private val inputState: InputState by inject()

    override fun keyUp(keycode: Int): Boolean {
        if (inputState.mode.value == InputState.Mode.DIALOGUE) {
            when (keycode) {
                Input.Keys.ENTER -> {
                    dialogueManager.advance()
                    return true
                }

                Input.Keys.UP -> {
                    dialogueManager.selectPreviousOption()
                    return true
                }

                Input.Keys.DOWN -> {
                    dialogueManager.selectNextOption()
                    return true
                }
            }
        }

        return false
    }
}
