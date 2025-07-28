package pro.piechowski.highschoolstory.dialogue

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class DialogueManager : KoinComponent {
    private var dialogue: Dialogue? = null
    private var currentSentenceId: SentenceId? = dialogue?.rootIds?.firstOrNull()
    private val dialogueUserInterface: DialogueUserInterface by inject()

    val currentSentence get() = dialogue?.sentences?.get(currentSentenceId)
    var currentOptionIdx: Int = 0

    fun startDialogue(dialogue: Dialogue) {
        this.dialogue = dialogue

        updateUserInterface()
    }

    fun advance() {
        currentSentenceId =
            currentSentenceId
                ?.let { dialogue?.sentences?.getValue(it) }
                ?.childIds[currentOptionIdx]

        updateUserInterface()

        currentOptionIdx = 0
    }

    fun endDialogue() {
        dialogue = null
        currentSentenceId = null

        updateUserInterface()
    }

    fun isInDialogue(): Boolean = dialogue != null

    fun selectPreviousOption() {
        if (currentOptionIdx == 0) {
            currentOptionIdx = (currentSentence?.childIds ?: dialogue?.rootIds)?.size ?: 0
        }

        currentOptionIdx--
    }

    fun selectNextOption() {
        if (currentOptionIdx == (((currentSentence?.childIds ?: dialogue?.rootIds)?.size?.let { it - 1 }) ?: 0)) {
            currentOptionIdx = 0
        }

        currentOptionIdx++
    }

    private fun updateUserInterface() =
        with(dialogueUserInterface) {
            dialogueBox.isVisible = dialogue != null

            dialogueOptions.isVisible = false
            ifChoice {
                dialogueOptions.isVisible = true
                dialogueOptions.clearItems()
                dialogueOptions.setItems(*it.toTypedArray())
            }

            dialogueLabel.setText(currentSentence?.line)
        }

    private fun <R> ifIsCurrentlyInDialogue(then: (dialogue: Dialogue) -> R) {
        if (dialogue != null) {
            then(dialogue!!)
        }
    }

    private fun ifChoice(then: (options: List<Dialogue.Sentence>) -> Unit) {
        if (dialogue != null && currentSentence == null && dialogue!!.rootIds.size > 1) {
            then(
                dialogue!!
                    .sentences
                    .filter { it.key in dialogue!!.rootIds }
                    .values
                    .toList(),
            )
        }

        if (dialogue != null && currentSentence != null && currentSentence!!.childIds.size > 1) {
            then(
                dialogue!!
                    .sentences
                    .filter { it.key in currentSentence!!.childIds }
                    .values
                    .toList(),
            )
        }
    }
}
