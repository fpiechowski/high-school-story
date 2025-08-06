package pro.piechowski.highschoolstory.debug.selection

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.github.quillraven.fleks.IntervalSystem
import ktx.graphics.use
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import pro.piechowski.highschoolstory.rendering.sprite.CurrentSprite

class DebugSelectionIndicatorRenderingSystem :
    IntervalSystem(),
    KoinComponent {
    private val debugEntitySelectionManager by inject<DebugEntitySelectionManager>()
    private val shapeRenderer by inject<ShapeRenderer>()

    override fun onTick() {
        with(world) {
            debugEntitySelectionManager.selectedEntity.value?.get(CurrentSprite)?.let { sprite ->
                shapeRenderer.use(ShapeRenderer.ShapeType.Line) {
                    it.color = Color.GREEN.cpy()
                    it.rect(sprite.sprite.x, sprite.sprite.y, sprite.sprite.width, sprite.sprite.height)
                }
            }
        }
    }
}
