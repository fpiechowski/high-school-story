package pro.piechowski.highschoolstory.transition

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.github.quillraven.fleks.IntervalSystem
import ktx.graphics.use
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import pro.piechowski.highschoolstory.camera.CameraSet

class FadeTransitionSystem :
    IntervalSystem(),
    KoinComponent {
    private val transitionManager by inject<TransitionManager>()
    private val shapeRenderer by inject<ShapeRenderer>()
    private val cameraSet by inject<CameraSet>()

    override fun onTick() {
        Gdx.gl.glEnable(GL20.GL_BLEND)
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA)

        transitionManager.currentTransition?.let { currentTransition ->
            if (currentTransition is Transition.FadeBlack) {
                transitionManager.update(deltaTime)

                val rectX = cameraSet.meterCamera.position.x - cameraSet.meterCamera.viewportWidth / 2
                val rectY = cameraSet.meterCamera.position.y - cameraSet.meterCamera.viewportHeight / 2

                shapeRenderer.use(ShapeRenderer.ShapeType.Filled, cameraSet.meterCamera) {
                    it.color =
                        Color.BLACK.cpy().apply {
                            a =
                                when (currentTransition) {
                                    is Transition.FadeBlack.In -> currentTransition.progress
                                    is Transition.FadeBlack.Out -> 1 - currentTransition.progress
                                }
                        }

                    it.rect(rectX, rectY, cameraSet.meterCamera.viewportWidth, cameraSet.meterCamera.viewportHeight)
                }
            }
        }
    }
}
