package pro.piechowski.highschoolstory.debug.selection

import com.badlogic.gdx.Input
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.github.quillraven.fleks.World
import ktx.app.KtxInputAdapter
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import pro.piechowski.highschoolstory.camera.MeterCamera
import pro.piechowski.highschoolstory.sprite.CurrentSprite

class DebugSelectionInputProcessor :
    KtxInputAdapter,
    KoinComponent {
    private val camera: MeterCamera by inject()
    private val world by inject<World>()
    private val debugEntitySelectionManager by inject<DebugEntitySelectionManager>()

    override fun touchDown(
        screenX: Int,
        screenY: Int,
        pointer: Int,
        button: Int,
    ): Boolean {
        if (button == Input.Buttons.LEFT) {
            val worldPoint =
                camera
                    .unproject(Vector3(screenX.toFloat(), screenY.toFloat(), 0f))
                    .let { Vector2(it.x, it.y) }

            debugEntitySelectionManager.currentSelectedEntity =
                with(world) {
                    family { all(CurrentSprite.Companion) }
                        .map { it to it[CurrentSprite.Companion] }
                        .filter { (_, currentSprite) -> currentSprite.sprite.boundingRectangle.contains(worldPoint) }
                        .minByOrNull { (_, currentSprite) -> currentSprite.sprite.y }
                        ?.first
                }

            return true
        }

        return false
    }
}
