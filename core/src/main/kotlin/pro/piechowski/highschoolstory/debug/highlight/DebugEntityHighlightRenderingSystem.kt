package pro.piechowski.highschoolstory.debug.highlight

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.github.quillraven.fleks.IntervalSystem
import ktx.graphics.use
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import pro.piechowski.highschoolstory.camera.MeterCamera
import pro.piechowski.highschoolstory.sprite.CurrentSprite

class DebugEntityHighlightRenderingSystem :
    IntervalSystem(),
    KoinComponent {
    private val debugEntityHighlightManager by inject<DebugEntityHighlightManager>()
    private val shapeRenderer by inject<ShapeRenderer>()
    private val meterCamera by inject<MeterCamera>()

    override fun onTick() {
        with(world) {
            debugEntityHighlightManager.highlighted.value.let { highlighted ->

                shapeRenderer.use(ShapeRenderer.ShapeType.Line, meterCamera) {
                    it.color = Color.YELLOW.cpy()
                    world
                        .family { all(CurrentSprite) }
                        .forEach { entity ->
                            val sprite = entity[CurrentSprite]

                            it.rect(sprite.sprite.x, sprite.sprite.y, sprite.sprite.width, sprite.sprite.height)
                        }
                }
            }
        }
    }
}
