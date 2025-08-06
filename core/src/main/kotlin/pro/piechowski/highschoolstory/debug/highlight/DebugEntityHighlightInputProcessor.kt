package pro.piechowski.highschoolstory.debug.highlight

import com.badlogic.gdx.Input
import com.github.quillraven.fleks.World
import ktx.app.KtxInputAdapter
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import pro.piechowski.highschoolstory.camera.PixelCamera

class DebugEntityHighlightInputProcessor :
    KtxInputAdapter,
    KoinComponent {
    private val camera: PixelCamera by inject()
    private val world by inject<World>()
    private val debugEntityHighlightManager by inject<DebugEntityHighlightManager>()

    override fun keyDown(keycode: Int): Boolean {
        if (keycode == Input.Keys.H) {
            debugEntityHighlightManager.highlightedValue = true

            return true
        }

        return false
    }

    override fun keyUp(keycode: Int): Boolean {
        if (keycode == Input.Keys.H) {
            debugEntityHighlightManager.highlightedValue = false

            return true
        }

        return false
    }
}
