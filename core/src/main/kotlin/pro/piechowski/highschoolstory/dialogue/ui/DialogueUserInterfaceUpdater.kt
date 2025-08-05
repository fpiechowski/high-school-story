package pro.piechowski.highschoolstory.dialogue.ui

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import pro.piechowski.highschoolstory.dialogue.Dialogue
import pro.piechowski.highschoolstory.dialogue.DialogueManager

class DialogueUserInterfaceUpdater : KoinComponent {
    private val dialogueUserInterface: DialogueUserInterface by inject()
    private val dialogueManager: DialogueManager by inject()

    fun updateUserInterface() =
        with(dialogueManager) {
            with(dialogueUserInterface) {
                dialogueBox.isVisible = dialogueState.value != null

                updateDialogueOptions()

                dialogueState.value?.let { state ->
                    if (state.currentNode is Dialogue.Node.Sentence) {
                        dialogueLabel.setText(state.currentNode.line)
                    }
                }
            }
        }

    private fun updateDialogueOptions() =
        with(dialogueManager) {
            with(dialogueUserInterface) {
                dialogueOptionsList.isVisible = false

                dialogueState.value?.let { state ->
                    if (state.currentNode is Dialogue.Node.Choice) {
                        dialogueOptionsList.apply {
                            isVisible = true
                            clearItems()
                            setItems(
                                *state.currentNode.options
                                    .map { it.line }
                                    .toTypedArray(),
                            )
                            selectedIndex = state.currentOptionIdx
                        }

                        scrollOnChoiceSelectionChange()
                    } else {
                        scrollToBeginningInstantly()
                    }
                }
            }
        }

    private fun scrollToBeginningInstantly() =
        with(dialogueUserInterface) {
            dialogueScrollPane.scrollTo(
                0f,
                dialogueScrollPaneBeginningYPosition,
                dialogueOptionsList.width,
                dialogueOptionsList.itemHeight,
            )
            dialogueScrollPane.updateVisualScroll()
        }

    private val dialogueScrollPaneBeginningYPosition
        get() =
            with(dialogueUserInterface) {
                dialogueOptionsList.height + dialogueLabel.prefHeight + with(DialogueUserInterface.dialogueLabelPadding) { top + bottom }
            }

    private fun scrollOnChoiceSelectionChange() =
        with(dialogueUserInterface) {
            val desiredScrollYPosition =
                if (dialogueOptionsList.selectedIndex == 0) {
                    dialogueScrollPaneBeginningYPosition
                } else {
                    dialogueOptionsList.height - dialogueOptionsList.selectedIndex * dialogueOptionsList.itemHeight
                }

            dialogueScrollPane.scrollTo(
                0f,
                desiredScrollYPosition,
                dialogueOptionsList.width,
                dialogueOptionsList.itemHeight,
            )
        }
}
