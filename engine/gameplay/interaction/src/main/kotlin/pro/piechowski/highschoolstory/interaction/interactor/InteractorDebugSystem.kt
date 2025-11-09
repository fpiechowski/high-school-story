package pro.piechowski.highschoolstory.interaction.interactor

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World
import ktx.graphics.circle
import ktx.graphics.use
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import pro.piechowski.highschoolstory.camera.MeterCamera
import pro.piechowski.highschoolstory.interaction.InteractionSystem

class InteractorDebugSystem :
    IteratingSystem(
        World.Interactors,
    ),
    KoinComponent {
    val shapeRenderer: ShapeRenderer by inject()
    val meterCamera: MeterCamera by inject()

    override fun onTickEntity(entity: Entity) {
        Gdx.gl.glEnable(GL20.GL_BLEND)
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA)

        val interactor = Interactor(entity)

        shapeRenderer.use(ShapeRenderer.ShapeType.Filled, meterCamera) {
            it.color = Color.BLUE.cpy().also { it.a = 0.1f }
            it.circle(
                interactor.position,
                InteractionSystem.interactionRange.value,
                64,
            )
        }
    }
}
