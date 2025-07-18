package pro.piechowski.highschoolstory.movement

import com.badlogic.gdx.math.Vector2
import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType

data class Position(
    val position: Vector2,
) : Component<Position> {
    override fun type() = Position

    companion object : ComponentType<Position>()
}
