package pro.piechowski.highschoolstory.direction

import com.badlogic.gdx.math.Vector2
import kotlinx.serialization.Contextual
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlin.reflect.jvm.jvmName

@Serializable
sealed class Direction(
    @Contextual open val vector: Vector2,
) : Vector2(vector.x, vector.y)

sealed class Direction4(
    override val vector: Vector2,
) : Direction(vector) {
    data object Up : Direction4(Vector2(0f, 1f)) {
        private fun readResolve(): Any = Up
    }

    data object Down : Direction4(Vector2(0f, -1f)) {
        private fun readResolve(): Any = Down
    }

    data object Left : Direction4(Vector2(-1f, 0f)) {
        private fun readResolve(): Any = Left
    }

    data object Right : Direction4(Vector2(1f, 0f)) {
        private fun readResolve(): Any = Right
    }

    companion object {
        fun from(vector: Vector2): Direction4 =
            when {
                vector.isZero(0.001f) -> throw IllegalArgumentException("Zero vector has no direction")
                kotlin.math.abs(vector.x) > kotlin.math.abs(vector.y) ->
                    if (vector.x > 0) Right else Left

                else -> if (vector.y > 0) Up else Down
            }
    }

    object Serializer : KSerializer<Direction4> {
        override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(Direction4::class.jvmName, PrimitiveKind.STRING)

        override fun serialize(
            encoder: Encoder,
            value: Direction4,
        ) {
            encoder.encodeString(value::class.jvmName)
        }

        override fun deserialize(decoder: Decoder): Direction4 =
            when (decoder.decodeString()) {
                Up::class.jvmName -> Up
                Down::class.jvmName -> Down
                Left::class.jvmName -> Left
                Right::class.jvmName -> Right
                else -> throw IllegalArgumentException("Unknown direction")
            }
    }
}

sealed class Direction8(
    override val vector: Vector2,
) : Direction(vector) {
    data object Up : Direction8(Vector2(0f, 1f)) {
        private fun readResolve(): Any = Up
    }

    data object Down : Direction8(Vector2(0f, -1f)) {
        private fun readResolve(): Any = Down
    }

    data object Left : Direction8(Vector2(-1f, 0f)) {
        private fun readResolve(): Any = Left
    }

    data object Right : Direction8(Vector2(1f, 0f)) {
        private fun readResolve(): Any = Right
    }

    data object UpLeft : Direction8(Vector2(-1f, 1f)) {
        private fun readResolve(): Any = UpLeft
    }

    data object UpRight : Direction8(Vector2(1f, 1f)) {
        private fun readResolve(): Any = UpRight
    }

    data object DownLeft : Direction8(Vector2(-1f, -1f)) {
        private fun readResolve(): Any = DownLeft
    }

    data object DownRight : Direction8(Vector2(1f, -1f)) {
        private fun readResolve(): Any = DownRight
    }

    fun toDirection4(): Direction4 =
        when (this) {
            Up, UpLeft, UpRight -> Direction4.Up
            Down, DownLeft, DownRight -> Direction4.Down
            Left -> Direction4.Left
            Right -> Direction4.Right
        }

    companion object {
        fun from(vector: Vector2): Direction8 {
            if (vector.isZero(0.001f)) throw IllegalArgumentException("Zero vector has no direction")
            val x = vector.x
            val y = vector.y

            return when {
                x > 0 && y > 0 -> UpRight
                x < 0 && y > 0 -> UpLeft
                x > 0 && y < 0 -> DownRight
                x < 0 && y < 0 -> DownLeft
                x == 0f && y > 0 -> Up
                x == 0f && y < 0 -> Down
                x > 0 && y == 0f -> Right
                x < 0 && y == 0f -> Left
                else -> throw IllegalArgumentException("Invalid direction")
            }
        }
    }

    object Serializer : KSerializer<Direction8> {
        override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(Direction8::class.jvmName, PrimitiveKind.STRING)

        override fun serialize(
            encoder: Encoder,
            value: Direction8,
        ) {
            encoder.encodeString(value::class.jvmName)
        }

        override fun deserialize(decoder: Decoder): Direction8 =
            when (decoder.decodeString()) {
                Up::class.jvmName -> Up
                UpLeft::class.jvmName -> UpLeft
                UpRight::class.jvmName -> UpRight
                Down::class.jvmName -> Down
                DownLeft::class.jvmName -> DownLeft
                DownRight::class.jvmName -> DownRight
                Left::class.jvmName -> Left
                Right::class.jvmName -> Right
                else -> throw IllegalArgumentException("Unknown direction")
            }
    }
}
