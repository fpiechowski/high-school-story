package pro.piechowski.highschoolstory

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.scenes.scene2d.Stage
import com.github.quillraven.fleks.World
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ktx.app.KtxScreen
import ktx.app.clearScreen
import ktx.assets.async.AssetStorage
import ktx.assets.disposeSafely
import ktx.async.KtxAsync
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.unloadKoinModules
import org.koin.core.module.Module
import pro.piechowski.highschoolstory.asset.AssetIdentifiers
import pro.piechowski.highschoolstory.camera.MeterCamera
import pro.piechowski.highschoolstory.camera.MeterViewport
import pro.piechowski.highschoolstory.camera.PixelCamera
import pro.piechowski.highschoolstory.camera.PixelViewport
import pro.piechowski.highschoolstory.input.GameInputMultiplexer
import pro.piechowski.highschoolstory.input.InputState
import pro.piechowski.highschoolstory.ui.UserInterface
import pro.piechowski.highschoolstory.ui.UserInterfaceViewport

class GameScreen :
    KtxScreen,
    KoinComponent {
    private val config: Config by inject()
    private val gameModule: Module by inject(gameModuleQualifier)
    private val gameInputMultiplexer: GameInputMultiplexer by inject()
    private val inputState: InputState by inject()

    init {
        Gdx.input.inputProcessor = gameInputMultiplexer
        inputState.mode.value = InputState.Mode.EXPLORATION
    }

    private val assetStorage: AssetStorage by inject()

    init {
        with(assetStorage) {
            loadSync<Texture>(AssetIdentifiers.Textures.PlayerCharacter)
            loadSync<Texture>(AssetIdentifiers.Textures.Character)
            loadSync<TiledMap>(AssetIdentifiers.Maps.Town)
        }
    }

    private val batch: SpriteBatch by inject()
    private val pixelCamera: PixelCamera by inject()
    private val meterCamera: MeterCamera by inject()
    private val pixelViewport: PixelViewport by inject()
    private val meterViewport: MeterViewport by inject()
    private val userInterfaceViewport: UserInterfaceViewport by inject()
    private val world: World by inject()
    private val stage: Stage by inject()
    private val userInterface: UserInterface by inject()
    private val gameInitializer: GameInitializer by inject()

    private var gameInitializationJob: Job =
        KtxAsync
            .launch {
                gameInitializer.initializeTestGame()
            }

    init {
        userInterface.addActors()
    }

    override fun render(delta: Float) {
        if (!gameInitializationJob.isCompleted) return

        clearScreen(red = 0.7f, green = 0.7f, blue = 0.7f)

        pixelCamera.update()
        meterCamera.update()

        batch.projectionMatrix = pixelCamera.combined

        world.update(delta)

        stage.act()
        stage.draw()
    }

    override fun dispose() {
        batch.disposeSafely()
        unloadKoinModules(gameModule)
    }

    override fun resize(
        width: Int,
        height: Int,
    ) {
        if (!gameInitializationJob.isCompleted) return

        pixelViewport.update(width, height)
        meterViewport.update(width, height)
        userInterfaceViewport.update(width, height)
    }
}
