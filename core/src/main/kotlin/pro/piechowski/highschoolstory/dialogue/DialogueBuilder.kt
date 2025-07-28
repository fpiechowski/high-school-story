package pro.piechowski.highschoolstory.dialogue

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import pro.piechowski.highschoolstory.dialogue.Dialogue.Sentence
import java.util.UUID

typealias SentenceId = String

// --- Core Model ---
data class Dialogue(
    val sentences: Map<SentenceId, Sentence>,
    val rootIds: List<SentenceId>,
) {
    data class Actor(
        val name: String,
    ) : Component<Actor> {
        override fun type(): ComponentType<Actor> = Actor

        companion object : ComponentType<Actor>()
    }

    data class Sentence(
        val id: SentenceId,
        val actor: Actor,
        val line: String,
        val parentId: SentenceId? = null,
        val childIds: List<SentenceId> = emptyList(),
    ) {
        val isChoice: Boolean get() = childIds.size > 1

        override fun toString(): String = "$id: ${actor.name}: $line"
    }
}

// --- DSL Builder ---
typealias LineBlock = DialogueBuilder.SentenceBuilder.(SentenceId) -> Unit

class DialogueBuilder {
    private val allSentences = mutableMapOf<SentenceId, Sentence>()
    private val rootIds = mutableListOf<SentenceId>()
    private val unresolvedLinks =
        mutableMapOf<SentenceId, MutableList<SentenceId>>() // id -> list of unresolved children ids

    private fun newId(): SentenceId = UUID.randomUUID().toString()

    fun Dialogue.Actor.says(
        line: String,
        id: SentenceId = newId(),
        block: LineBlock? = null,
    ): SentenceId {
        val sentence = Sentence(id, this, line)
        allSentences[id] = sentence
        rootIds.add(id)
        block?.let { SentenceBuilder(id).it(id) }
        return id
    }

    inner class SentenceBuilder(
        private val parentId: SentenceId,
    ) {
        fun Dialogue.Actor.says(
            line: String,
            id: SentenceId = newId(),
            block: LineBlock? = null,
        ): SentenceId {
            val sentence = Sentence(id, this, line, parentId)
            allSentences[id] = sentence
            addChild(parentId, id)
            block?.let { SentenceBuilder(id).it(id) }
            return id
        }

        fun goTo(targetId: SentenceId) {
            addChild(parentId, targetId)
        }

        private fun addChild(
            parentId: SentenceId,
            childId: SentenceId,
        ) {
            val sentence = allSentences[parentId]
            if (sentence != null) {
                val updated = sentence.copy(childIds = sentence.childIds + childId)
                allSentences[parentId] = updated
            } else {
                unresolvedLinks.computeIfAbsent(parentId) { mutableListOf() }.add(childId)
            }
        }
    }

    fun build(): Dialogue {
        // Resolve any delayed links
        for ((parentId, children) in unresolvedLinks) {
            val sentence =
                allSentences[parentId]
                    ?: error("Unresolved parent sentence ID: $parentId")
            allSentences[parentId] = sentence.copy(childIds = sentence.childIds + children)
        }
        return Dialogue(allSentences, rootIds)
    }
}

fun dialogue(block: DialogueBuilder.() -> Unit): Dialogue = DialogueBuilder().apply(block).build()
