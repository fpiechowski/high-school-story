package pro.piechowski.highschoolstory.dialogue

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ktx.async.KtxAsync
import ktx.async.newSingleThreadAsyncContext
import ktx.async.onRenderingThread
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import pro.piechowski.highschoolstory.input.InputState
import kotlin.reflect.jvm.jvmName

class DialogueManager : KoinComponent {
    private val currentDialogueState = MutableStateFlow<DialogueState?>(null)

    private val asyncContext = newSingleThreadAsyncContext(DialogueManager::class.jvmName)

    private val inputState: InputState by inject()
    private val dialogueUserInterfaceUpdater: DialogueUserInterfaceUpdater by inject()

    init {
        KtxAsync.launch(asyncContext) {
            currentDialogueState.collect {
                if (it == null) {
                    inputState.mode.value = InputState.Mode.EXPLORATION
                } else {
                    inputState.mode.value = InputState.Mode.DIALOGUE

                    if (it.currentNode is Dialogue.Node.End) {
                        currentDialogueState.update { null }
                    }
                }

                onRenderingThread {
                    dialogueUserInterfaceUpdater.updateUserInterface()
                }
            }
        }
    }

    fun startDialogue(dialogue: Dialogue) = currentDialogueState.update { DialogueState(dialogue) }

    fun advance() =
        currentDialogueState.update {
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

    fun selectNextOption() = currentDialogueState.update { it?.withNextOptionSelected() }

    fun selectPreviousOption() = currentDialogueState.update { it?.withPreviousOptionSelected() }

    val dialogueState: StateFlow<DialogueState?> get() = currentDialogueState.asStateFlow()
}
