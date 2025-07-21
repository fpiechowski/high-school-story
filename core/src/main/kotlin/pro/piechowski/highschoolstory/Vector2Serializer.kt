package pro.piechowski.highschoolstory

import com.badlogic.gdx.math.Vector2
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializer(forClass = Vector2::class)
object Vector2Serializer : KSerializer<Vector2> {
    override fun serialize(
        encoder: Encoder,
        value: Vector2,
    ) {
        TODO("Not yet implemented")
    }

    override fun deserialize(decoder: Decoder): Vector2 {
        TODO("Not yet implemented")
    }

    override val descriptor: SerialDescriptor
        get() = TODO("Not yet implemented")
}
