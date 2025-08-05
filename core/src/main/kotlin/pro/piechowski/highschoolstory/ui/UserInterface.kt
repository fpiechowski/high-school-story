package pro.piechowski.highschoolstory.ui

import com.badlogic.gdx.scenes.scene2d.Stage
import ktx.scene2d.actors
import ktx.scene2d.table
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import pro.piechowski.highschoolstory.dialogue.ui.DialogueUserInterface

class UserInterface : KoinComponent {
    private val stage: Stage by inject()
    private val dialogueUserInterface: DialogueUserInterface by inject()

    fun addActors() =
        stage.actors {
            table { tableActor ->
                setFillParent(true)
                defaults().pad(10f)

                row()

                bottom()

                add(dialogueUserInterface.dialogueBox)
                    .height(stage.viewport.worldHeight * 0.25f)
                    .expandX()
                    .fill()
            }
        }
}
