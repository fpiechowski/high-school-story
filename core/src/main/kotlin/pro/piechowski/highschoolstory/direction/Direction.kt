package pro.piechowski.highschoolstory.direction

import com.badlogic.gdx.math.Vector2
import kotlinx.serialization.Serializable
import kotlin.math.abs

@Serializable
sealed class Direction(
    val x: Float,
    val y: Float,
) : Vector2(x, y) {
    companion object {
        fun from(vector: Vector2): Direction {
            if (vector.isZero(0.0001f)) throw IllegalArgumentException("Zero vector has no direction")

            return if (abs(vector.x) > abs(vector.y)) {
                if (vector.x > 0) Right else Left
            } else {
                if (vector.y > 0) Up else Down
            }
        }
    }

    @Serializable
    data object Up : Direction(0f, 1f) {
        private fun readResolve(): Any = Up
    }

    @Serializable
    data object Down : Direction(0f, -1f) {
        private fun readResolve(): Any = Down
    }

    @Serializable
    data object Left : Direction(-1f, 0f) {
        private fun readResolve(): Any = Left
    }

    @Serializable
    data object Right : Direction(1f, 0f) {
        private fun readResolve(): Any = Right
    }
}
