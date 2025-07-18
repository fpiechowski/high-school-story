package pro.piechowski.highschoolstory.movement

import com.badlogic.gdx.math.Vector2
import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType

data class Velocity(
    var velocity: Vector2 = Vector2.Zero.cpy(),
) : Component<Velocity> {
    override fun type() = Velocity

    companion object : ComponentType<Velocity>()
}
