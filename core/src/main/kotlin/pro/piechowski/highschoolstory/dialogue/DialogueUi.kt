package pro.piechowski.highschoolstory.dialogue

import com.badlogic.gdx.scenes.scene2d.Stage
import ktx.scene2d.label
import ktx.scene2d.scene2d
import ktx.scene2d.scrollPane
import ktx.scene2d.table
import pro.piechowski.highschoolstory.ui.ScrollPaneStyle

context(stage: Stage)
val dialogueUi
    get() =
        scene2d
            .table {
                debugTable()

                scrollPane(style = ScrollPaneStyle.FRAME) { cell ->
                    cell
                        .fill()
                        .expand()
                        .maxWidth(stage.viewport.worldWidth * 0.75f)
                    setScrollingDisabled(true, true)

                    table {
                        setFillParent(true)

                        scrollPane {
                            it
                                .fill()
                                .expand()
                                .pad(20f)

                            label(
                                """
                                Lorem ipsum dolor sit amet, consectetur adipiscing elit,
                                sed do eiusmod tempor incididunt ut labore et dolore magna
                                aliqua. Ut enim ad minim veniam, quis nostrud exercitation
                                ullamco laboris nisi ut aliquip ex ea commodo consequat.
                                Duis aute irure dolor in reprehenderit in voluptate velit
                                esse cillum dolore eu fugiat nulla pariatur. Excepteur sint
                                occaecat cupidatat non proident, sunt in culpa qui officia
                                deserunt mollit anim id est laborum.
                                Lorem ipsum dolor sit amet, consectetur adipiscing elit,
                                sed do eiusmod tempor incididunt ut labore et dolore magna
                                aliqua. Ut enim ad minim veniam, quis nostrud exercitation
                                ullamco laboris nisi ut aliquip ex ea commodo consequat.
                                Duis aute irure dolor in reprehenderit in voluptate velit
                                esse cillum dolore eu fugiat nulla pariatur. Excepteur sint
                                occaecat cupidatat non proident, sunt in culpa qui officia
                                deserunt mollit anim id est laborum.
                                """.trimIndent(),
                            )
                        }
                    }
                }
            }
