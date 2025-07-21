package pro.piechowski.highschoolstory.animation

import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.Sprite
import pro.piechowski.highschoolstory.direction.Direction4

data class Direction4Animations(
    val up: Animation<out Sprite>,
    val right: Animation<out Sprite>,
    val down: Animation<out Sprite>,
    val left: Animation<out Sprite>,
) {
    operator fun get(direction: Direction4) =
        when (direction) {
            Direction4.Up -> up
            Direction4.Right -> right
            Direction4.Down -> down
            Direction4.Left -> left
        }
}
