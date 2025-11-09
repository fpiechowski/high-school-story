package pro.piechowski.highschoolstory.dialogue

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.updateAndGet
import kotlinx.coroutines.launch
import ktx.async.KtxAsync
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import pro.piechowski.highschoolstory.CoroutineContexts
import pro.piechowski.highschoolstory.input.InputManager
import pro.piechowski.highschoolstory.input.InputOwner
import pro.piechowski.highschoolstory.ui.dialogue.DialogueUserInterfaceUpdater

class DialogueManager :
    KoinComponent,
    InputOwner {
    private val _currentDialogueState = MutableStateFlow<DialogueState?>(null)
    val currentDialogueState = _currentDialogueState.asStateFlow()

    private val inputManager: InputManager by inject()

    init {
        KtxAsync.launch(CoroutineContexts.Logic) {
            _currentDialogueState.collect { currentDialogueState ->
                when {
                    currentDialogueState == null && inputManager.owner.value == this -> inputManager.revokeOwnership()
                    currentDialogueState != null -> {
                        inputManager.passOwnership(this@DialogueManager)

                        if (currentDialogueState.currentNode is Dialogue.Node.End) {
                            _currentDialogueState.value = null
                            currentDialogueState.job.complete()
                        }
                    }
                }
            }
        }
    }

    fun startDialogue(dialogue: Dialogue): DialogueState =
        DialogueState(dialogue).also {
            _currentDialogueState.value = it
        }

    fun advance(): DialogueState? =
        _currentDialogueState.updateAndGet {
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

    fun selectNextOption(): DialogueState? = _currentDialogueState.updateAndGet { it?.withNextOptionSelected() }

    fun selectPreviousOption(): DialogueState? = _currentDialogueState.updateAndGet { it?.withPreviousOptionSelected() }
}

suspend fun DialogueState.await() = job.join()
