package pro.piechowski.highschoolstory.movement.facedirection

import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World
import ktx.graphics.use
import ktx.math.plus
import ktx.math.times
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import pro.piechowski.highschoolstory.interaction.InteractionSystem
import pro.piechowski.highschoolstory.movement.position.Position

class FaceDirectionDebugSystem :
    IteratingSystem(World.family { all(FaceDirection, Position) }),
    KoinComponent {
    private val shapeRenderer: ShapeRenderer by inject()
    private val camera: Camera by inject()

    override fun onTickEntity(entity: Entity) {
        val position = entity[Position].position

        shapeRenderer.use(ShapeRenderer.ShapeType.Line, camera) {
            it.color = Color.YELLOW.cpy()
            it.line(position, position + entity[FaceDirection].faceDirection.nor() * InteractionSystem.INTERACTION_RANGE)
        }
    }
}
