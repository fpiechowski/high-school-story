package pro.piechowski.highschoolstory.dialogue

import kotlinx.coroutines.flow.MutableStateFlow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import pro.piechowski.highschoolstory.input.InputState

class DialogueManager : KoinComponent {
    val dialogue: MutableStateFlow<Dialogue?> = MutableStateFlow(null)
    val currentSentenceId: MutableStateFlow<SentenceId?> = MutableStateFlow(null)

    private val dialogueUserInterfaceUpdater: DialogueUserInterfaceUpdater by inject()
    private val inputState: InputState by inject()

    val currentSentence get() = dialogue.value?.sentences?.get(currentSentenceId.value)
    val currentOptionIdx: MutableStateFlow<Int> = MutableStateFlow(0)

    fun startDialogue(dialogue: Dialogue) {
        this.dialogue.value = dialogue

        if (dialogue.rootIds.size == 1) {
            advance()
        }

        dialogueUserInterfaceUpdater.updateUserInterface()

        inputState.mode.value = InputState.Mode.DIALOGUE
    }

    fun advance() {
        ifIsCurrentlyInDialogue { dialogue ->
            ifIsAlreadyAdvanced(
                dialogue,
                then = { sentence ->
                    currentSentenceId.value = sentence.childIds.getOrNull(currentOptionIdx.value)
                },
                otherwise = {
                    currentSentenceId.value = dialogue.rootIds.getOrNull(currentOptionIdx.value)
                },
            )

            if (currentSentenceId.value == null) endDialogue()
        }

        dialogueUserInterfaceUpdater.updateUserInterface()

        currentOptionIdx.value = 0
    }

    fun endDialogue() {
        dialogue.value = null
        currentSentenceId.value = null
        currentOptionIdx.value = 0

        dialogueUserInterfaceUpdater.updateUserInterface()

        inputState.mode.value = InputState.Mode.EXPLORATION
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

        dialogueUserInterfaceUpdater.updateUserInterface()
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

        dialogueUserInterfaceUpdater.updateUserInterface()
    }

    fun <R> ifIsCurrentlyInDialogue(
        otherwise: () -> R? = { null },
        then: (dialogue: Dialogue) -> R,
    ) = dialogue.value?.let { then(it) } ?: otherwise()

    internal fun <R> ifIsAlreadyAdvanced(
        dialogue: Dialogue,
        then: (sentence: Dialogue.Sentence) -> R,
        otherwise: () -> R? = { null },
    ) = currentSentenceId.value?.let {
        then(dialogue.sentences[it] ?: sentenceNotFoundError(it))
    } ?: otherwise()

    internal fun <R> ifChoice(
        then: (options: List<Dialogue.Sentence>) -> R,
        otherwise: () -> R? = { null },
    ) {
        ifIsCurrentlyInDialogue(
            then = { dialogue ->
                ifIsAlreadyAdvanced(
                    dialogue,
                    then = { sentence ->
                        val options = dialogue.sentences.filter { it.key in sentence.childIds }.values
                        if (options.size > 1) {
                            then(options.toList())
                        } else {
                            otherwise()
                        }
                    },
                    otherwise = {
                        val options = dialogue.sentences.filter { it.key in dialogue.rootIds }.values
                        if (options.size > 1) {
                            then(options.toList())
                        } else {
                            otherwise()
                        }
                    },
                )
            },
            otherwise = { otherwise() },
        )
    }

    private fun sentenceNotFoundError(string: SentenceId): Nothing {
        error("Sentence with id $string not found in dialogue")
    }
}
