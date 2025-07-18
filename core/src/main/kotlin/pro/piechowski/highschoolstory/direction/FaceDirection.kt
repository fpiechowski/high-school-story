package pro.piechowski.highschoolstory.direction

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import kotlinx.serialization.Serializable

@Serializable
data class FaceDirection(
    var faceDirection: Direction,
) : Component<FaceDirection> {
    override fun type() = FaceDirection

    companion object : ComponentType<FaceDirection>()
}
