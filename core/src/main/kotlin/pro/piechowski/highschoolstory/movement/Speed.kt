package pro.piechowski.highschoolstory.movement

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType

data class Speed(
    val speed: Float = 0f,
) : Component<Speed> {
    override fun type() = Speed

    companion object : ComponentType<Speed>()
}
