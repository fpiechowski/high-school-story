package pro.piechowski.highschoolstory.movement

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.math.Vector2
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World
import io.github.oshai.kotlinlogging.KotlinLogging
import pro.piechowski.highschoolstory.debug

class ControllerMovementInputSystem : IteratingSystem(World.family { all(MovementInput.Controller) }) {
    private val logger = KotlinLogging.logger { }

    override fun onTickEntity(entity: Entity) {
        val controllerMovementInput = entity[MovementInput.Controller]

        controllerMovementInput.movementInput = Vector2.Zero.cpy()

        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            controllerMovementInput.movementInput.y += 1f
        }

        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            controllerMovementInput.movementInput.y -= 1f
        }

        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            controllerMovementInput.movementInput.x -= 1f
        }

        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            controllerMovementInput.movementInput.x += 1f
        }

        if (controllerMovementInput.movementInput != Vector2.Zero.cpy()) {
            logger.debug(controllerMovementInput, entity)
        }
    }
}
