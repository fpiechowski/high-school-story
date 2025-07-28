package pro.piechowski.highschoolstory.dialogue

import com.badlogic.gdx.Input
import ktx.app.KtxInputAdapter
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class DialogueInputProcessor :
    KtxInputAdapter,
    KoinComponent {
    private val dialogueManager: DialogueManager by inject()

    override fun keyUp(keycode: Int): Boolean {
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

        return false
    }
}
