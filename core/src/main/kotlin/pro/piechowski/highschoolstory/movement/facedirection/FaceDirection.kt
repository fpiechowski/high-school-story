package pro.piechowski.highschoolstory.movement.facedirection

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import pro.piechowski.highschoolstory.direction.Direction8

@Serializable
data class FaceDirection(
    @Contextual var faceDirection: Direction8,
) : Component<FaceDirection> {
    override fun type() = FaceDirection

    companion object : ComponentType<FaceDirection>()
}
