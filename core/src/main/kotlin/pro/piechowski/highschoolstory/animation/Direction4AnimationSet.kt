package pro.piechowski.highschoolstory.animation

import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.Sprite
import pro.piechowski.highschoolstory.direction.Direction4Mapping

data class Direction4AnimationSet(
    override val up: Animation<out Sprite>,
    override val down: Animation<out Sprite>,
    override val left: Animation<out Sprite>,
    override val right: Animation<out Sprite>,
) : Direction4Mapping<Animation<out Sprite>>(up, down, left, right)
