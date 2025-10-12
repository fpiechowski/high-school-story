package pro.piechowski.highschoolstory.light

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.github.quillraven.fleks.IntervalSystem
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import ktx.graphics.use
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import pro.piechowski.highschoolstory.camera.PixelCamera
import pro.piechowski.highschoolstory.camera.PixelViewportManager
import pro.piechowski.highschoolstory.light.framebuffer.LightFrameBufferManager
import pro.piechowski.highschoolstory.shader.ShaderManager
import pro.piechowski.highschoolstory.sprite.framebuffer.SpriteFrameBufferManager
import pro.piechowski.highschoolstory.time.calendar.Calendar
import pro.piechowski.highschoolstory.time.clock.Clock
import pro.piechowski.highschoolstory.weather.Weather
import pro.piechowski.highschoolstory.weather.WeatherManager
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.pow

class SunlightPostProcessingRenderingSystem :
    IntervalSystem(),
    KoinComponent {
    private val logger = KotlinLogging.logger { }

    private val spriteFrameBufferManager by inject<SpriteFrameBufferManager>()
    private val lightFrameBufferManager by inject<LightFrameBufferManager>()
    private val spriteBatch by inject<SpriteBatch>()
    private val pixelCamera by inject<PixelCamera>()
    private val pixelViewportManager by inject<PixelViewportManager>()

    private val clock by inject<Clock>()
    private val calendar by inject<Calendar>()
    private val weatherManager by inject<WeatherManager>()

    private val shaderManager by inject<ShaderManager>()

    override fun onTick() {
        spriteBatch.shader = null
        spriteBatch.begin()
        spriteBatch.draw(
            TextureRegion(lightFrameBufferManager.frameBuffer.value.colorBufferTexture).apply { flip(false, true) },
            0f,
            0f,
            Gdx.graphics.width.toFloat(),
            Gdx.graphics.height.toFloat(),
        )
        spriteBatch.end()

        val spriteRegion =
            TextureRegion(spriteFrameBufferManager.frameBuffer.value.colorBufferTexture).apply { flip(false, true) }
        val lightRegion =
            TextureRegion(lightFrameBufferManager.frameBuffer.value.colorBufferTexture).apply { flip(false, true) }

        val gain = 1.2f

        val factor = ambientFactorForCompositeRealistic(calendar.currentDate, clock.currentTime, weatherManager.currentWeather)
        val tint = ambientTintRealisticWeather(calendar.currentDate, clock.currentTime, weatherManager.currentWeather)
        val ambientR = tint.r * factor
        val ambientG = tint.g * factor
        val ambientB = tint.b * factor

        with(shaderManager) {
            // spriteBatch.shader = lightingCompositeShader
            spriteBatch.use {
                lightingCompositeShader.setUniformi("u_scene", 0)
                lightingCompositeShader.setUniformi("u_light", 1)
                lightingCompositeShader.setUniformf("u_ambientColor", ambientR, ambientG, ambientB)
                lightingCompositeShader.setUniformf("u_gain", gain)

                spriteRegion.texture.bind(0)
                Gdx.gl.glActiveTexture(GL20.GL_TEXTURE1)
                lightRegion.texture.bind(1)
                Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0)

                pixelViewportManager.pixelViewportValue.apply()
                spriteBatch.projectionMatrix = pixelCamera.combined

                pixelViewportManager.pixelViewportValue.let { pixelViewport ->
                    spriteBatch.draw(
                        spriteRegion,
                        pixelCamera.position.x - (pixelCamera.viewportWidth / 2),
                        pixelCamera.position.y - (pixelCamera.viewportHeight / 2),
                        pixelCamera.viewportWidth,
                        pixelCamera.viewportHeight,
                    )

                    spriteBatch.draw(
                        lightRegion,
                        pixelCamera.position.x - (pixelCamera.viewportWidth / 2),
                        pixelCamera.position.y - (pixelCamera.viewportHeight / 2),
                        pixelCamera.viewportWidth,
                        pixelCamera.viewportHeight,
                    )
                }
            }
        }

        spriteBatch.shader = null
    }

    private fun ambientFactorForCompositeRealistic(
        date: LocalDate,
        time: LocalTime,
        weather: Weather,
    ): Float {
        val doy = date.dayOfYear
        val yearLen = 365.0
        val daysFromSolstice = abs(doy - 172).toDouble() // June 21 ~ 172

        // Seasonal day length: 8h (winter) … 16h (summer)
        val dayLen = 12.0 + 4.0 * cos((daysFromSolstice / (yearLen / 2.0)) * Math.PI)
        val sunrise = 12.0 - dayLen / 2.0
        val sunset = 12.0 + dayLen / 2.0
        val t = time.toSecondOfDay() / 3600.0

        val mid = (sunrise + sunset) / 2.0
        val half = dayLen / 2.0

        // Day curve: 1 at noon, 0 at sunrise/sunset, 0 at night
        val inDay = t in sunrise..sunset
        val x = ((t - mid) / half).coerceIn(-1.0, 1.0) // -1..1 during day
        val dayCurve = if (inDay) cos((Math.PI / 2) * x).coerceAtLeast(0.0) else 0.0

        // Slight gamma to keep mornings/evenings a bit brighter
        val gamma = 0.7
        val shaped = dayCurve.pow(gamma)

        // Weather-adjusted max daylight brightness
        val dayMax =
            when (weather) {
                Weather.CLEAR -> 1.00f
                Weather.CLOUDY -> 0.85f
                Weather.OVERCAST -> 0.70f
                Weather.RAIN -> 0.60f
                Weather.STORM -> 0.50f
            }
        val nightMin = 0.06f // fully dark night

        // Interpolate between nightMin and dayMax
        return (nightMin + (dayMax - nightMin) * shaped).coerceIn(0.0, 1.0).toFloat()
    }

    private fun ambientTintRealisticWeather(
        date: LocalDate,
        time: LocalTime,
        weather: Weather,
    ): Color {
        val dayOfYear = date.dayOfYear
        val yearLen = 365.0
        val daysFromSolstice = abs(dayOfYear - 172).toDouble() // ~June 21 ~ day 172

        // Seasonal day length: ~8h winter … ~16h summer
        val dayLen = 12.0 + 4.0 * cos((daysFromSolstice / (yearLen / 2.0)) * Math.PI)
        val sunrise = 12.0 - dayLen / 2.0
        val sunset = 12.0 + dayLen / 2.0

        val t = time.toSecondOfDay() / 3600.0 // hours
        val isDay = t in sunrise..sunset

        fun smooth(
            a: Double,
            b: Double,
            x: Double,
        ): Double {
            val u = ((x - a) / (b - a)).coerceIn(0.0, 1.0)
            return u * u * (3 - 2 * u) // smoothstep
        }

        // 45-minute transitions (twilight)
        val dawnStart = sunrise - 0.75
        val dawnEnd = sunrise + 0.75
        val duskStart = sunset - 0.75
        val duskEnd = sunset + 0.75

        val dawnAmt = smooth(dawnStart, dawnEnd, t) * (1 - smooth(sunrise, sunrise + 0.01, t))
        val duskAmt = smooth(duskStart, duskEnd, t) * (1 - smooth(sunset, sunset + 0.01, t))
        val dayAmt = if (isDay) (1 - smooth(dawnStart, dawnEnd, t)) * (1 - smooth(duskStart, duskEnd, t)) else 0.0
        val nightAmt = (1.0 - dawnAmt - dayAmt - duskAmt).coerceIn(0.0, 1.0)

        // Pure mood colors (no intensity baked in)
        val night = floatArrayOf(0.85f, 0.90f, 1.10f) // subtle blue
        val dawn = floatArrayOf(1.05f, 0.98f, 0.93f) // gentle warm
        val day = floatArrayOf(1.00f, 1.00f, 1.00f) // neutral
        val dusk = floatArrayOf(1.04f, 0.97f, 0.92f) // gentle warm

        var r = (night[0] * nightAmt + dawn[0] * dawnAmt + day[0] * dayAmt + dusk[0] * duskAmt).toFloat()
        var g = (night[1] * nightAmt + dawn[1] * dawnAmt + day[1] * dayAmt + dusk[1] * duskAmt).toFloat()
        var b = (night[2] * nightAmt + dawn[2] * dawnAmt + day[2] * dayAmt + dusk[2] * duskAmt).toFloat()

        // Apply slight weather color bias
        // This only shifts hue/saturation, not brightness
        when (weather) {
            Weather.CLEAR -> { /* no tint change */ }
            Weather.CLOUDY -> { // slightly desaturate, cool
                val gray = (r + g + b) / 3f
                r = gray + (r - gray) * 0.95f
                g = gray + (g - gray) * 0.95f
                b = gray + (b - gray) * 0.98f
            }
            Weather.OVERCAST -> { // more desaturation, cool blue shift
                val gray = (r + g + b) / 3f
                r = gray + (r - gray) * 0.90f
                g = gray + (g - gray) * 0.92f
                b = gray + (b - gray) * 1.02f
            }
            Weather.RAIN -> { // strong cool shift
                val gray = (r + g + b) / 3f
                r = gray + (r - gray) * 0.85f
                g = gray + (g - gray) * 0.90f
                b = gray + (b - gray) * 1.05f
            }
            Weather.STORM -> { // heavy cool & desaturation
                val gray = (r + g + b) / 3f
                r = gray + (r - gray) * 0.80f
                g = gray + (g - gray) * 0.85f
                b = gray + (b - gray) * 1.08f
            }
        }

        return Color(r, g, b, 1f)
    }
}
