package pro.piechowski.highschoolstory.dialogue

import kotlinx.coroutines.flow.MutableStateFlow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import pro.piechowski.highschoolstory.input.InputState

class DialogueManager : KoinComponent {
    var dialogue: Dialogue? = null
    var currentNode: Dialogue.Node? = null

    private val dialogueUserInterfaceUpdater: DialogueUserInterfaceUpdater by inject()
    private val inputState: InputState by inject()

    val currentOptionIdx: MutableStateFlow<Int> = MutableStateFlow(0)

    fun startDialogue(dialogue: Dialogue) {
        inputState.mode.value = InputState.Mode.DIALOGUE

        this.dialogue = dialogue
        this.currentNode = dialogue.root

        dialogueUserInterfaceUpdater.updateUserInterface()
    }

    fun advance() {
        when (currentNode) {
            is Dialogue.Node.Sentence -> currentNode = (currentNode as Dialogue.Node.Sentence).nextNode
            is Dialogue.Node.Choice ->
                currentNode =
                    (currentNode as Dialogue.Node.Choice).options[currentOptionIdx.value].nextNode

            else -> Unit
        }

        if (currentNode is Dialogue.Node.GoTo) {
            currentNode = dialogue?.allNodes?.get((currentNode as Dialogue.Node.GoTo).targetId)
        }

        if (currentNode == null) {
            endDialogue()
        }

        dialogueUserInterfaceUpdater.updateUserInterface()
    }

    fun endDialogue() {
        inputState.mode.value = InputState.Mode.EXPLORATION

        dialogue = null
        currentNode = null
        currentOptionIdx.value = 0

        dialogueUserInterfaceUpdater.updateUserInterface()
    }

    fun selectPreviousOption() {
        if (currentNode is Dialogue.Node.Choice) {
            currentOptionIdx.value = if (currentOptionIdx.value > 0) currentOptionIdx.value - 1 else 0
        }

        dialogueUserInterfaceUpdater.updateUserInterface()
    }

    fun selectNextOption() {
        if (currentNode is Dialogue.Node.Choice) {
            currentOptionIdx.value =
                if (currentOptionIdx.value < (currentNode as Dialogue.Node.Choice).options.lastIndex) {
                    currentOptionIdx.value + 1
                } else {
                    0
                }
        }

        dialogueUserInterfaceUpdater.updateUserInterface()
    }
}
