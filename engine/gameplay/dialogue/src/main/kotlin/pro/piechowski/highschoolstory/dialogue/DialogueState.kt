package pro.piechowski.highschoolstory.dialogue

import kotlinx.coroutines.CompletableJob
import kotlinx.coroutines.Job
import org.koin.core.component.KoinComponent

data class DialogueState(
    val dialogue: Dialogue,
    val currentNode: Dialogue.Node = dialogue.root,
    val currentOptionIdx: Int = 0,
    val job: CompletableJob = Job(),
) : KoinComponent {
    fun advanced(): DialogueState {
        val eventualNextSentenceOrChoiceNode =
            when (currentNode) {
                is Dialogue.Node.Sentence -> currentNode.nextNode
                is Dialogue.Node.Choice ->
                    currentNode.options[currentOptionIdx].nextNode
                else -> currentNode
            }

        val eventualGoToTargetNode =
            when (eventualNextSentenceOrChoiceNode) {
                is Dialogue.Node.GoTo ->
                    dialogue.allNodes[eventualNextSentenceOrChoiceNode.targetId]
                        ?: error("No node with id ${eventualNextSentenceOrChoiceNode.targetId}")
                else -> eventualNextSentenceOrChoiceNode
            }

        return copy(currentNode = eventualGoToTargetNode, currentOptionIdx = 0)
    }

    fun withPreviousOptionSelected(): DialogueState =
        if (currentNode is Dialogue.Node.Choice) {
            val previousOptionIdx = if (currentOptionIdx > 0) currentOptionIdx - 1 else currentNode.options.lastIndex
            copy(currentOptionIdx = previousOptionIdx)
        } else {
            this
        }

    fun withNextOptionSelected(): DialogueState =
        if (currentNode is Dialogue.Node.Choice) {
            val nextOptionIdx =
                if (currentOptionIdx < currentNode.options.lastIndex) {
                    currentOptionIdx + 1
                } else {
                    0
                }
            copy(currentOptionIdx = nextOptionIdx)
        } else {
            this
        }
}
