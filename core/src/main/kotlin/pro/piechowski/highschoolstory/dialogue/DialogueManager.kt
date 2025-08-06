package pro.piechowski.highschoolstory.dialogue

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ktx.async.KtxAsync
import ktx.async.onRenderingThread
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import pro.piechowski.highschoolstory.CoroutineContexts
import pro.piechowski.highschoolstory.dialogue.ui.DialogueUserInterfaceUpdater
import pro.piechowski.highschoolstory.input.InputState

class DialogueManager : KoinComponent {
    private val _currentDialogueState = MutableStateFlow<DialogueState?>(null)
    val currentDialogueState: StateFlow<DialogueState?> get() = _currentDialogueState.asStateFlow()

    private val inputState: InputState by inject()
    private val dialogueUserInterfaceUpdater: DialogueUserInterfaceUpdater by inject()

    init {
        KtxAsync.launch(CoroutineContexts.Logic) {
            _currentDialogueState.collect {
                if (it == null) {
                    inputState.mode.value = InputState.Mode.EXPLORATION
                } else {
                    inputState.mode.value = InputState.Mode.DIALOGUE

                    if (it.currentNode is Dialogue.Node.End) {
                        _currentDialogueState.value = null
                    }
                }

                onRenderingThread {
                    dialogueUserInterfaceUpdater.updateUserInterface()
                }
            }
        }
    }

    fun startDialogue(dialogue: Dialogue) {
        _currentDialogueState.value = DialogueState(dialogue)
    }

    fun advance() =
        _currentDialogueState.update {
            it
                ?.also {
                    when (it.currentNode) {
                        is Dialogue.Node.Sentence -> it.currentNode.onAdvanced.invoke()
                        is Dialogue.Node.Choice ->
                            it.currentNode.options[it.currentOptionIdx]
                                .onAdvanced
                                .invoke()

                        else -> Unit
                    }
                }?.advanced()
        }

    fun selectNextOption() = _currentDialogueState.update { it?.withNextOptionSelected() }

    fun selectPreviousOption() = _currentDialogueState.update { it?.withPreviousOptionSelected() }
}
