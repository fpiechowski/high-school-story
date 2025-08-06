package pro.piechowski.highschoolstory.dialogue

import org.junit.jupiter.api.Test

class DialogueBuilderTest {
    @Test
    fun `builds dialogue with forward and backward goTo references`() {
        val actorA = Dialogue.Actor("Actor A")
        val actorB = Dialogue.Actor("Actor B")

        val dialogue =
            dialogue {
                actorA.says(
                    line = "line",
                    id = "first",
                    nextNode =
                        actorB.choice(id = "choice") {
                            option(line = "option 1", nextNode = goTo("someId"))
                            option(line = "option 2")
                            option(line = "option 3")
                        },
                )
            }
    }
}
