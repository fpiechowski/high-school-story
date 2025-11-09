package pro.piechowski.highschoolstory

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.github.quillraven.fleks.World
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import ktx.app.KtxScreen
import ktx.app.clearScreen
import ktx.assets.disposeSafely
import ktx.async.KtxAsync
import org.koin.core.annotation.KoinInternalApi
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.unloadKoinModules
import org.koin.core.module.Module
import pro.piechowski.highschoolstory.camera.MeterCamera
import pro.piechowski.highschoolstory.camera.MeterViewport
import pro.piechowski.highschoolstory.input.GameInputMultiplexer
import pro.piechowski.highschoolstory.input.InputManager
import pro.piechowski.highschoolstory.ui.UserInterfaceViewport

@ExperimentalCoroutinesApi
@ExperimentalContextParameters
@KoinInternalApi
class GameScreen :
    KtxScreen,
    KoinComponent {
    private val gameInputMultiplexer: GameInputMultiplexer by inject()

    init {
        Gdx.input.inputProcessor = gameInputMultiplexer
    }

    private val batch: SpriteBatch by inject()
    private val meterCamera by inject<MeterCamera>()
    private val meterViewport: MeterViewport by inject()
    private val userInterfaceViewport: UserInterfaceViewport by inject()
    private val world: World by inject()

    private val entrypoint: Entrypoint by inject()

    init {
        KtxAsync.launch {
            entrypoint.run()
        }
    }

    override fun render(delta: Float) {
        clearScreen(red = 0.7f, green = 0.7f, blue = 0.7f)

        meterCamera.update()

        batch.projectionMatrix = meterCamera.combined

        world.update(delta)
    }

    override fun dispose() {
        batch.disposeSafely()
        unloadKoinModules(coreModule())
    }

    override fun resize(
        width: Int,
        height: Int,
    ) {
        meterViewport.update(width, height)
        userInterfaceViewport.update(width, height)
    }
}
