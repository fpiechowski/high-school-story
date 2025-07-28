package pro.piechowski.highschoolstory.dialogue

import com.badlogic.gdx.scenes.scene2d.Stage
import ktx.scene2d.label
import ktx.scene2d.listWidget
import ktx.scene2d.scene2d
import ktx.scene2d.scrollPane
import ktx.scene2d.table
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import pro.piechowski.highschoolstory.ui.ScrollPaneStyle

class DialogueUserInterface : KoinComponent {
    private val stage: Stage by inject()

    val dialogueLabel = scene2d.label("")

    val dialogueOptions =
        scene2d.listWidget<Dialogue.Sentence> {
            isVisible = false
        }

    val dialogueBox =
        scene2d.table {
            isVisible = false
            debugTable()

            scrollPane(style = ScrollPaneStyle.FRAME) { cell ->
                cell
                    .fill()
                    .expand()
                    .maxWidth(this@DialogueUserInterface.stage.viewport.worldWidth * 0.75f)
                setScrollingDisabled(true, true)

                table {
                    setFillParent(true)

                    scrollPane {
                        it
                            .fill()
                            .expand()
                            .pad(20f)

                        table {
                            addActor(dialogueLabel)
                            row()
                            addActor(dialogueOptions)
                        }
                    }
                }
            }
        }
}
