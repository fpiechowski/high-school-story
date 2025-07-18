package pro.piechowski.highschoolstory

import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.viewport.Viewport
import com.github.quillraven.fleks.World
import ktx.app.KtxScreen
import ktx.app.clearScreen
import ktx.assets.async.AssetStorage
import ktx.assets.disposeSafely
import ktx.graphics.use
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import pro.piechowski.highschoolstory.character.PlayerCharacter

class GameScreen : KtxScreen, KoinComponent {
    init {
        loadKoinModules(gameModule)
    }

    private val assetStorage: AssetStorage by inject()

    init {
        assetStorage.loadSync<Texture>(AssetIdentifiers.Textures.PlayerCharacter)
    }

    private val batch: SpriteBatch by inject()
    private val camera: Camera by inject()
    private val viewport: Viewport by inject()
    private val world: World by inject()

    init {
        with(world) {
            with (assetStorage) {
                PlayerCharacter.Factory.create()
            }
        }
    }

    override fun render(delta: Float) {
        batch.projectionMatrix = camera.combined

        camera.update()

        clearScreen(red = 0.7f, green = 0.7f, blue = 0.7f)

        batch.use {
            world.update(delta)
        }
    }

    override fun dispose() {
        batch.disposeSafely()
        unloadKoinModules(gameModule)
    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height)
    }
}
