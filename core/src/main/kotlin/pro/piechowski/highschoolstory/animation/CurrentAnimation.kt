package pro.piechowski.highschoolstory.animation

import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.Sprite
import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType

class CurrentAnimation(
    var animation: Animation<out Sprite>,
    var time: Float = 0f,
) : Component<CurrentAnimation> {
    override fun type() = CurrentAnimation

    companion object : ComponentType<CurrentAnimation>()
}
