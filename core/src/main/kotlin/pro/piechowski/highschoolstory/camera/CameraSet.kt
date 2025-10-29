package pro.piechowski.highschoolstory.camera

import com.badlogic.gdx.math.Vector2
import ktx.graphics.moveTo
import pro.piechowski.highschoolstory.physics.Meter

data class CameraSet(
    val meterCamera: MeterCamera,
) {
    fun moveTo(
        x: Meter,
        y: Meter,
    ) {
        meterCamera.moveTo(Vector2(x.value, y.value))
    }

    fun zoom(amount: Float) {
        meterCamera.zoom *= amount

        update()
    }

    fun update() {
        meterCamera.update()
    }
}
