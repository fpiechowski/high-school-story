package pro.piechowski.highschoolstory.dialogue

import kotlinx.coroutines.flow.MutableStateFlow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class DialogueManager : KoinComponent {
    private val dialogue: MutableStateFlow<Dialogue?> = MutableStateFlow(null)
    private val currentSentenceId: MutableStateFlow<SentenceId?> = MutableStateFlow(null)
    private val dialogueUserInterface: DialogueUserInterface by inject()

    val currentSentence get() = dialogue.value?.sentences?.get(currentSentenceId.value)
    val currentOptionIdx: MutableStateFlow<Int> = MutableStateFlow(0)

    fun startDialogue(dialogue: Dialogue) {
        this.dialogue.value = dialogue

        updateUserInterface()
    }

    fun advance() {
        ifIsCurrentlyInDialogue { dialogue ->
            ifIsAlreadyAdvanced(
                dialogue,
                then = { sentence ->
                    currentSentenceId.value = sentence.childIds[currentOptionIdx.value]
                },
                otherwise = {
                    currentSentenceId.value = dialogue.rootIds[currentOptionIdx.value]
                },
            )

            if (currentSentenceId.value == null) endDialogue()
        }

        updateUserInterface()

        currentOptionIdx.value = 0
    }

    fun endDialogue() {
        dialogue.value = null
        currentSentenceId.value = null
        currentOptionIdx.value = 0

        updateUserInterface()
    }

    fun selectPreviousOption() {
        if (currentOptionIdx.value == 0) {
            ifIsCurrentlyInDialogue { dialogue ->
                ifIsAlreadyAdvanced(
                    dialogue,
                    then = { sentence ->
                        currentOptionIdx.value = sentence.childIds.lastIndex
                    },
                    otherwise = {
                        currentOptionIdx.value = dialogue.rootIds.lastIndex
                    },
                )
            }
        } else {
            currentOptionIdx.value--
        }
    }

    fun selectNextOption() {
        ifIsCurrentlyInDialogue { dialogue ->
            ifIsAlreadyAdvanced(
                dialogue,
                then = { sentence ->
                    if (currentOptionIdx.value == sentence.childIds.lastIndex) {
                        currentOptionIdx.value = 0
                    } else {
                        currentOptionIdx.value++
                    }
                },
                otherwise = {
                    if (currentOptionIdx.value == dialogue.rootIds.lastIndex) {
                        currentOptionIdx.value = 0
                    } else {
                        currentOptionIdx.value++
                    }
                },
            )
        }
    }

    fun <R> ifIsCurrentlyInDialogue(then: (dialogue: Dialogue) -> R) = dialogue.value?.let { then(it) }

    private fun updateUserInterface() =
        with(dialogueUserInterface) {
            dialogueBox.isVisible = dialogue.value != null

            dialogueOptions.isVisible = false
            ifChoice {
                dialogueOptions.isVisible = true
                dialogueOptions.clearItems()
                dialogueOptions.setItems(*it.toTypedArray())
            }

            dialogueLabel.setText(currentSentence?.line)
        }

    private fun <R> ifIsAlreadyAdvanced(
        dialogue: Dialogue,
        then: (sentence: Dialogue.Sentence) -> R,
        otherwise: () -> R? = { null },
    ) = currentSentenceId.value?.let {
        then(dialogue.sentences[it] ?: sentenceNotFoundError(it))
    } ?: otherwise()

    private fun ifChoice(then: (options: List<Dialogue.Sentence>) -> Unit) {
        ifIsCurrentlyInDialogue { dialogue ->
            ifIsAlreadyAdvanced(
                dialogue,
                then = { sentence ->
                    val options = dialogue.sentences.filter { it.key in sentence.childIds }.values
                    if (options.size > 1) {
                        then(options.toList())
                    }
                },
                otherwise = {
                    val options = dialogue.sentences.filter { it.key in dialogue.rootIds }.values
                    if (options.size > 1) {
                        then(options.toList())
                    }
                },
            )
        }
    }

    private fun sentenceNotFoundError(string: SentenceId): Nothing {
        error("Sentence with id $string not found in dialogue")
    }
}
