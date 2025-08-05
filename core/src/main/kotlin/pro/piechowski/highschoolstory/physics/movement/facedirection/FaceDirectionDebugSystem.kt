package pro.piechowski.highschoolstory.physics.movement.facedirection

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World
import ktx.graphics.use
import ktx.math.plus
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import pro.piechowski.highschoolstory.camera.PixelCamera
import pro.piechowski.highschoolstory.interaction.InteractionSystem
import pro.piechowski.highschoolstory.physics.body.PhysicsBody
import pro.piechowski.highschoolstory.physics.m
import pro.piechowski.highschoolstory.physics.times

class FaceDirectionDebugSystem :
    IteratingSystem(World.family { all(FaceDirection, PhysicsBody) }),
    KoinComponent {
    private val shapeRenderer: ShapeRenderer by inject()
    private val pixelCamera: PixelCamera by inject()

    override fun onTickEntity(entity: Entity) {
        val position = entity[PhysicsBody].body.position * m.toPixels()

        shapeRenderer.use(ShapeRenderer.ShapeType.Line, pixelCamera) {
            it.color = Color.YELLOW.cpy()
            it.line(
                position,
                position + entity[FaceDirection].faceDirection.nor() * InteractionSystem.interactionRange.toPixels(),
            )
        }
    }
}
