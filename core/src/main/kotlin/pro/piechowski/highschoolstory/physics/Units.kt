package pro.piechowski.highschoolstory.physics

import com.badlogic.gdx.math.Vector2
import kotlinx.serialization.Serializable

const val PIXELS_PER_METER = 64
const val METERS_PER_PIXEL = 1 / PIXELS_PER_METER.toFloat()

@JvmInline
value class Pixel(
    val value: Float,
) {
    fun toMeter() = Meter(value * METERS_PER_PIXEL)

    operator fun plus(other: Pixel) = Pixel(value + other.value)

    operator fun minus(other: Pixel) = Pixel(value - other.value)

    override fun toString() = "$value px"
}

@JvmInline
value class Meter(
    val value: Float,
) {
    fun toPixels() = Pixel(value * PIXELS_PER_METER)

    operator fun plus(other: Meter) = Meter(value + other.value)

    operator fun minus(other: Meter) = Meter(value - other.value)

    operator fun times(float: Float) = Meter(value * float)

    operator fun div(float: Float) = Meter(value / float)

    operator fun div(seconds: Second): MetersPerSeconds = MetersPerSeconds(value / seconds.value)

    operator fun compareTo(other: Meter): Int = (value - other.value).toInt()

    operator fun unaryMinus(): Meter = Meter(-value)

    override fun toString() = "$value m"
}

@JvmInline
value class Second(
    val value: Float,
) {
    override fun toString() = "$value s"
}

@JvmInline
@Serializable
value class MetersPerSeconds(
    val value: Float,
) {
    operator fun times(seconds: Second) = Meter(value * seconds.value)

    operator fun times(float: Float) = MetersPerSeconds(value * float)

    operator fun plus(mps: MetersPerSeconds) = MetersPerSeconds(value + mps.value)

    operator fun compareTo(other: MetersPerSeconds): Int = (value - other.value).toInt()

    override fun toString(): String = "$value m/s"
}

val Float.m get() = Meter(this)

val Float.px get() = Pixel(this)

val Int.px get() = Pixel(this.toFloat())

val Float.s get() = Second(this)

val Float.mps get() = MetersPerSeconds(this)

val m = Meter(1f)

val px = Pixel(1f)

val s = Second(1f)

val mps = MetersPerSeconds(1f)

infix operator fun Float.times(meters: Meter) = meters * this

infix operator fun Float.times(metersPerSeconds: MetersPerSeconds) = mps * this

infix operator fun Vector2.times(metersPerSeconds: MetersPerSeconds): Vector2 = this.cpy().scl(metersPerSeconds.value)

infix operator fun Vector2.times(meters: Meter): Vector2 = this.cpy().scl(meters.value)

infix operator fun Vector2.times(pixels: Pixel): Vector2 = this.cpy().scl(pixels.value)
