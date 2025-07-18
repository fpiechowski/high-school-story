package pro.piechowski.highschoolstory.animation

import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.Sprite
import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

class CurrentAnimation(
    var animation: Animation<Sprite>,
    var time: Float = 0f,
) : Component<CurrentAnimation> {
    override fun type() = CurrentAnimation

    companion object : ComponentType<CurrentAnimation>()
}
