package pro.piechowski.highschoolstory.light

import box2dLight.RayHandler
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.github.quillraven.fleks.IntervalSystem
import io.github.oshai.kotlinlogging.KotlinLogging
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import pro.piechowski.highschoolstory.camera.MeterCamera

class LightRenderingSystem :
    IntervalSystem(),
    KoinComponent {
    private val rayHandler by inject<RayHandler>()
    private val meterCamera by inject<MeterCamera>()

    private val logger = KotlinLogging.logger { }

    override fun onTick() {
        /*Gdx.gl.glClearColor(0f, 0f, 0f, 0f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        Gdx.gl.glEnable(GL20.GL_BLEND)
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE)

        rayHandler.setCombinedMatrix(meterCamera)
        rayHandler.setAmbientLight(0f, 0f, 0f, 1f)
        rayHandler.updateAndRender()

        logger.debug { "Light rendering system rendered" }*/
    }
}
