package pro.piechowski.highschoolstory.dialogue

import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class DialogueBuilderTest {
    @Test
    fun `builds dialogue with forward and backward goTo references`() {
        val actorA = Dialogue.Actor("Actor A")
        val actorB = Dialogue.Actor("Actor B")

        val dialogue =
            dialogue {
                val quest = "quest"
                val howAreYou =
                    actorA.says("How are you?", "howAreYou") { howAreYou ->
                        actorB.says("I'm fine, thanks.", "fine") {
                            actorA.says("Then I have a quest for you.", "thenQuest") {
                                goTo(quest)
                            }
                        }
                        actorB.says("It's been better.", "bad") {
                            actorA.says("Why? What happened?", "why") {
                                actorB.says("I just have a bad day. Ask me again.", "again") {
                                    goTo(howAreYou)
                                }
                            }
                        }
                    }

                actorA.says("I have a quest for you.", "quest") {
                    actorA.says("I will ask how are you first.", "intro") {
                        goTo(howAreYou)
                    }
                }
            }

        dialogue.rootIds shouldContainExactly listOf("howAreYou", "quest")

        dialogue.sentences["howAreYou"] shouldBe
            Dialogue.Sentence(
                id = "howAreYou",
                actor = actorA,
                line = "How are you?",
                parentId = null,
                childIds = listOf("fine", "bad"),
            )

        dialogue.sentences["fine"] shouldBe
            Dialogue.Sentence(
                id = "fine",
                actor = actorB,
                line = "I'm fine, thanks.",
                parentId = "howAreYou",
                childIds = listOf("thenQuest"),
            )

        dialogue.sentences["thenQuest"] shouldBe
            Dialogue.Sentence(
                id = "thenQuest",
                actor = actorA,
                line = "Then I have a quest for you.",
                parentId = "fine",
                childIds = listOf("quest"), // goTo(quest)
            )

        dialogue.sentences["bad"] shouldBe
            Dialogue.Sentence(
                id = "bad",
                actor = actorB,
                line = "It's been better.",
                parentId = "howAreYou",
                childIds = listOf("why"),
            )

        dialogue.sentences["why"] shouldBe
            Dialogue.Sentence(
                id = "why",
                actor = actorA,
                line = "Why? What happened?",
                parentId = "bad",
                childIds = listOf("again"),
            )

        dialogue.sentences["again"] shouldBe
            Dialogue.Sentence(
                id = "again",
                actor = actorB,
                line = "I just have a bad day. Ask me again.",
                parentId = "why",
                childIds = listOf("howAreYou"), // circular goTo
            )

        dialogue.sentences["quest"] shouldBe
            Dialogue.Sentence(
                id = "quest",
                actor = actorA,
                line = "I have a quest for you.",
                parentId = null,
                childIds = listOf("intro"),
            )

        dialogue.sentences["intro"] shouldBe
            Dialogue.Sentence(
                id = "intro",
                actor = actorA,
                line = "I will ask how are you first.",
                parentId = "quest",
                childIds = listOf("howAreYou"),
            )
    }
}
