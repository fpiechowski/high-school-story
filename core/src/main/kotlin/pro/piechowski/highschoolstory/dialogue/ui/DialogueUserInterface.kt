package pro.piechowski.highschoolstory.dialogue.ui

import com.badlogic.gdx.scenes.scene2d.Stage
import com.kotcrab.vis.ui.building.utilities.Padding
import ktx.scene2d.label
import ktx.scene2d.listWidget
import ktx.scene2d.scene2d
import ktx.scene2d.scrollPane
import ktx.scene2d.table
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import pro.piechowski.highschoolstory.ui.ListStyle
import pro.piechowski.highschoolstory.ui.ScrollPaneStyle

class DialogueUserInterface : KoinComponent {
    private val stage: Stage by inject()

    val dialogueLabel = scene2d.label("")

    companion object {
        val dialogueLabelPadding =
            Padding(
                2f,
                2f,
                10f,
                2f,
            )
    }

    val dialogueOptionsList =
        scene2d.listWidget<String>(ListStyle.DIALOGUE_OPTIONS) {
            isVisible = false
            this.style.selection.topHeight = 10f
            this.style.selection.leftWidth = 10f
            this.style.selection.rightWidth = 10f
            this.style.selection.bottomHeight = 10f
        }

    val dialogueScrollPane =
        scene2d.scrollPane {
            table {
                debugTable()
                add(dialogueLabel)
                    .top()
                    .left()
                    .pad(
                        dialogueLabelPadding.top,
                        dialogueLabelPadding.left,
                        dialogueLabelPadding.bottom,
                        dialogueLabelPadding.right,
                    ).expand()
                row()
                add(dialogueOptionsList)
                    .top()
                    .left()
                    .expand()
            }
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
                    debugTable()
                    add(dialogueScrollPane)
                        .fill()
                        .expand()
                        .pad(20f)
                }
            }
        }
}
