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
import pro.piechowski.highschoolstory.camera.MeterCamera
import pro.piechowski.highschoolstory.interaction.InteractionSystem
import pro.piechowski.highschoolstory.physics.body.PhysicsBody
import pro.piechowski.highschoolstory.physics.m
import pro.piechowski.highschoolstory.physics.times

class FaceDirectionDebugSystem :
    IteratingSystem(World.family { all(PhysicsBody).any(FaceDirection4, FaceDirection8) }),
    KoinComponent {
    private val shapeRenderer: ShapeRenderer by inject()
    private val meterCamera: MeterCamera by inject()

    override fun onTickEntity(entity: Entity) {
        val position = entity[PhysicsBody].body.position

        shapeRenderer.use(ShapeRenderer.ShapeType.Line, meterCamera.combined) {
            it.color = Color.YELLOW.cpy()
            it.line(
                position,
                position + entity[FaceDirection].faceDirection.nor() * InteractionSystem.interactionRange,
            )
        }
    }
}
