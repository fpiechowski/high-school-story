package pro.piechowski.highschoolstory.physics.movement.facedirection

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.EntityComponentContext
import kotlinx.serialization.Contextual
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import pro.piechowski.highschoolstory.direction.Direction
import pro.piechowski.highschoolstory.direction.Direction4
import pro.piechowski.highschoolstory.direction.Direction8
import kotlin.reflect.jvm.jvmName

@Serializable
sealed class FaceDirection<T : Direction>(
    @Contextual open var faceDirection: T,
) {
    companion object
}

context(ecc: EntityComponentContext)
operator fun Entity.get(faceDirection: FaceDirection.Companion): FaceDirection<*> =
    with(ecc) {
        this@get.getOrNull(FaceDirection8) ?: this@get.getOrNull(FaceDirection4)
            ?: throw IllegalStateException("Interactor must have either 4 or 8 face directions")
    }

class FaceDirection4(
    override var faceDirection: Direction4,
) : FaceDirection<Direction4>(faceDirection),
    Component<FaceDirection4> {
    override fun type(): ComponentType<FaceDirection4> = FaceDirection4

    companion object : ComponentType<FaceDirection4>()

    object Serializer : KSerializer<FaceDirection4> {
        override val descriptor: SerialDescriptor =
            PrimitiveSerialDescriptor(
                FaceDirection4::class.jvmName,
                PrimitiveKind.STRING,
            )

        override fun serialize(
            encoder: Encoder,
            value: FaceDirection4,
        ) = Direction4.Serializer.serialize(encoder, value.faceDirection)

        override fun deserialize(decoder: Decoder): FaceDirection4 = FaceDirection4(Direction4.Serializer.deserialize(decoder))
    }
}

class FaceDirection8(
    override var faceDirection: Direction8,
) : FaceDirection<Direction8>(faceDirection),
    Component<FaceDirection8> {
    override fun type(): ComponentType<FaceDirection8> = FaceDirection8

    companion object : ComponentType<FaceDirection8>()

    object Serializer : KSerializer<FaceDirection8> {
        override val descriptor: SerialDescriptor =
            PrimitiveSerialDescriptor(
                FaceDirection8::class.jvmName,
                PrimitiveKind.STRING,
            )

        override fun serialize(
            encoder: Encoder,
            value: FaceDirection8,
        ) = Direction8.Serializer.serialize(encoder, value.faceDirection)

        override fun deserialize(decoder: Decoder): FaceDirection8 = FaceDirection8(Direction8.Serializer.deserialize(decoder))
    }
}
