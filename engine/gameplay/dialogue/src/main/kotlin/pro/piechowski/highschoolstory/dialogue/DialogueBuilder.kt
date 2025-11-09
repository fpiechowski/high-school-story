package pro.piechowski.highschoolstory.dialogue

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import kotlinx.coroutines.Job

typealias w = String

data class Dialogue(
    val root: Node,
    val allNodes: Map<String, Node> = emptyMap(),
) {
    sealed interface Node {
        data class Sentence(
            val id: String? = null,
            val actor: Actor,
            val line: String,
            val onAdvanced: () -> Unit = {},
            val nextNode: Node = End,
        ) : Node

        data class Choice(
            val id: String? = null,
            val actor: Actor,
            val options: List<Option>,
        ) : Node {
            data class Option(
                val line: String,
                val onAdvanced: () -> Unit = {},
                val nextNode: Node = End,
            )
        }

        data class GoTo(
            val targetId: String,
        ) : Node

        data object End : Node
    }

    data class Actor(
        val name: String,
    ) : Component<Actor> {
        override fun type(): ComponentType<Actor> = Actor

        companion object : ComponentType<Actor>()
    }
}

// --- DSL ---
fun dialogue(init: DialogueBuilder.() -> Dialogue.Node): Dialogue {
    val builder = DialogueBuilder()
    val root = builder.init()
    return Dialogue(root, builder.nodeMap)
}

class DialogueBuilder {
    internal val nodeMap = mutableMapOf<String, Dialogue.Node>()

    fun Dialogue.Actor.says(
        line: String,
        id: String? = null,
        onAdvanced: () -> Unit = {},
        nextNode: Dialogue.Node = Dialogue.Node.End,
    ): Dialogue.Node {
        val node = Dialogue.Node.Sentence(id, this, line, onAdvanced, nextNode)
        id?.let { nodeMap[it] = node }
        return node
    }

    fun Dialogue.Actor.choice(
        id: String? = null,
        block: DialogueChoiceBuilder.() -> Unit,
    ): Dialogue.Node.Choice {
        val builder = DialogueChoiceBuilder(id, this)
        builder.block()
        return builder
            .build()
            .also { node ->
                id?.let { nodeMap[it] = node }
            }
    }

    fun goTo(id: String): Dialogue.Node = Dialogue.Node.GoTo(id)
}

class DialogueChoiceBuilder(
    private val id: String? = null,
    private val actor: Dialogue.Actor,
    private val onAdvanced: () -> Unit = {},
) {
    private val options = mutableListOf<Dialogue.Node.Choice.Option>()

    fun option(
        line: String,
        onAdvanced: () -> Unit = {},
        nextNode: Dialogue.Node = Dialogue.Node.End,
    ) {
        options += Dialogue.Node.Choice.Option(line, onAdvanced, nextNode)
    }

    fun build(): Dialogue.Node.Choice = Dialogue.Node.Choice(id, actor, options)
}
