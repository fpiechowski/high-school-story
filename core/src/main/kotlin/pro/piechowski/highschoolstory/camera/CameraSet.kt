package pro.piechowski.highschoolstory.camera

import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.math.Vector2
import ktx.graphics.moveTo
import pro.piechowski.highschoolstory.physics.Meter
import pro.piechowski.highschoolstory.physics.Pixel

data class CameraSet(
    val pixelCamera: PixelCamera,
    val meterCamera: MeterCamera,
) {
    fun moveTo(
        x: Pixel,
        y: Pixel,
    ) {
        pixelCamera.moveTo(Vector2(x.value, y.value))
        meterCamera.moveTo(Vector2(x.toMeter().value, y.toMeter().value))
    }

    fun moveTo(
        x: Meter,
        y: Meter,
    ) {
        pixelCamera.moveTo(Vector2(x.toPixels().value, y.toPixels().value))
        meterCamera.moveTo(Vector2(x.value, y.value))
    }
}
