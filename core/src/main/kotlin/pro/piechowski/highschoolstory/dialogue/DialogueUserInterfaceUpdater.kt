package pro.piechowski.highschoolstory.dialogue

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class DialogueUserInterfaceUpdater : KoinComponent {
    private val dialogueUserInterface: DialogueUserInterface by inject()
    private val dialogueManager: DialogueManager by inject()

    fun updateUserInterface() =
        with(dialogueManager) {
            with(dialogueUserInterface) {
                dialogueBox.isVisible = dialogue != null

                updateDialogueOptions()

                if (currentNode is Dialogue.Node.Sentence) {
                    dialogueLabel.setText((currentNode as Dialogue.Node.Sentence)?.line)
                }
            }
        }

    private fun updateDialogueOptions() =
        with(dialogueManager) {
            with(dialogueUserInterface) {
                dialogueOptionsList.isVisible = false

                if (currentNode is Dialogue.Node.Choice) {
                    dialogueOptionsList.apply {
                        isVisible = true
                        clearItems()
                        setItems(*(currentNode as Dialogue.Node.Choice).options.map { it.line }.toTypedArray())
                        selectedIndex = currentOptionIdx.value
                    }

                    scrollOnChoiceSelectionChange()
                } else {
                    scrollToBeginningInstantly()
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
