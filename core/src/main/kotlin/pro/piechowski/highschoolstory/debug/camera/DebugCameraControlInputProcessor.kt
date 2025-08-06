package pro.piechowski.highschoolstory.debug.camera

import com.badlogic.gdx.Input
import com.badlogic.gdx.math.Vector2
import ktx.app.KtxInputAdapter
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import pro.piechowski.highschoolstory.camera.CameraSet
import pro.piechowski.highschoolstory.physics.px

class DebugCameraControlInputProcessor :
    KtxInputAdapter,
    KoinComponent {
    private val cameraSet by inject<CameraSet>()

    private var dragging = false
    private var previousDragPosition: Vector2? = null

    override fun touchDown(
        screenX: Int,
        screenY: Int,
        pointer: Int,
        button: Int,
    ): Boolean {
        if (button == Input.Buttons.MIDDLE) {
            previousDragPosition = Vector2(screenX.toFloat(), screenY.toFloat())
            dragging = true
            return true
        }

        return false
    }

    override fun touchUp(
        screenX: Int,
        screenY: Int,
        pointer: Int,
        button: Int,
    ): Boolean {
        if (button == Input.Buttons.MIDDLE) {
            previousDragPosition = null
            dragging = false
            return true
        }

        return false
    }

    override fun touchDragged(
        screenX: Int,
        screenY: Int,
        pointer: Int,
    ): Boolean {
        if (dragging) {
            previousDragPosition?.let { prev ->
                val current = Vector2(screenX.toFloat(), screenY.toFloat())

                // Calculate delta from last frame
                val delta = current.cpy().sub(prev).scl(SPEED)

                // Invert Y because screen coords are flipped vs world coords
                delta.y = -delta.y

                // Move camera by delta
                val camPos = cameraSet.pixelCamera.position
                cameraSet.moveTo((camPos.x - delta.x).px, (camPos.y - delta.y).px)

                // Update previous position for next frame
                previousDragPosition = current
            }
            return true
        }
        return false
    }

    override fun scrolled(
        amountX: Float,
        amountY: Float,
    ): Boolean {
        cameraSet.zoom(amountY * ZOOM_AMOUNT_MODIFIER)

        return true
    }

    companion object {
        const val ZOOM_AMOUNT_MODIFIER = 0.2f
        const val SPEED = 1f
    }
}
