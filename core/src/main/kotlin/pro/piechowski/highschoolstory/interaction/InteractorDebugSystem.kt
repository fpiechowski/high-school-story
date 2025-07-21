package pro.piechowski.highschoolstory.interaction

import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World
import ktx.graphics.circle
import ktx.graphics.use
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class InteractorDebugSystem :
    IteratingSystem(
        World.Interactors,
    ),
    KoinComponent {
    val shapeRenderer: ShapeRenderer by inject()
    val camera: Camera by inject()

    override fun onTickEntity(entity: Entity) {
        val interactor = InteractorEntity(entity)

        shapeRenderer.use(ShapeRenderer.ShapeType.Filled, camera) {
            it.color = Color.BLUE.cpy().also { it.a = 0.1f }
            it.circle(interactor.position.position, InteractionSystem.INTERACTION_RANGE)
        }
    }
}
