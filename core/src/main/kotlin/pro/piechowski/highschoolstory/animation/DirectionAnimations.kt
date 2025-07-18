package pro.piechowski.highschoolstory.animation

import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.Sprite
import pro.piechowski.highschoolstory.direction.Direction

data class DirectionAnimations(
    val up: Animation<Sprite>,
    val right: Animation<Sprite>,
    val down: Animation<Sprite>,
    val left: Animation<Sprite>,
) {
    operator fun get(direction: Direction) = when (direction) {
        Direction.Up -> up
        Direction.Right -> right
        Direction.Down -> down
        Direction.Left -> left
    }
}
