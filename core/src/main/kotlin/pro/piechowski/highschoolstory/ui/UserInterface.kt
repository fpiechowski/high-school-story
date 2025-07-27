package pro.piechowski.highschoolstory.ui

import com.badlogic.gdx.scenes.scene2d.Stage
import ktx.scene2d.actors
import ktx.scene2d.table
import pro.piechowski.highschoolstory.dialogue.dialogueUi

context(stage: Stage)
fun addActors() =
    stage.actors {
        table { tableActor ->
            setFillParent(true)
            defaults().pad(10f)

            row()

            bottom()

            add(dialogueUi)
                .height(stage.viewport.worldHeight * 0.25f)
                .expandX()
                .fill()
        }
    }
