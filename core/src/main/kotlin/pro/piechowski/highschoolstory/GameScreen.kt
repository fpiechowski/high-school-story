package pro.piechowski.highschoolstory

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.viewport.Viewport
import com.github.quillraven.fleks.World
import ktx.app.KtxScreen
import ktx.app.clearScreen
import ktx.assets.async.AssetStorage
import ktx.assets.disposeSafely
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import pro.piechowski.highschoolstory.character.Character
import pro.piechowski.highschoolstory.character.PlayerCharacter
import pro.piechowski.highschoolstory.input.GameInputMultiplexer
import pro.piechowski.highschoolstory.interaction.Interactable
import pro.piechowski.highschoolstory.movement.position.Position

class GameScreen :
    KtxScreen,
    KoinComponent {
    init {
        loadKoinModules(gameModule)
    }

    private val gameInputMultiplexer: GameInputMultiplexer by inject()

    init {
        Gdx.input.inputProcessor = gameInputMultiplexer
    }

    private val assetStorage: AssetStorage by inject()

    init {
        assetStorage.loadSync<Texture>(AssetIdentifiers.Textures.PlayerCharacter)
        assetStorage.loadSync<Texture>(AssetIdentifiers.Textures.Character)
    }

    private val batch: SpriteBatch by inject()
    private val camera: Camera by inject()
    private val viewport: Viewport by inject()
    private val world: World by inject()

    init {
        with(world) {
            with(assetStorage) {
                entity {
                    it += PlayerCharacter.archetype()
                }

                entity {
                    it += Character.archetype(AssetIdentifiers.Textures.Character)
                    it += Position(Vector2(300f, 100f))
                    it +=
                        Interactable {
                            println("Interacting with the character!")
                        }
                }
            }
        }
    }

    override fun render(delta: Float) {
        Gdx.gl.glEnable(GL20.GL_BLEND)
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA)

        batch.projectionMatrix = camera.combined

        camera.update()

        clearScreen(red = 0.7f, green = 0.7f, blue = 0.7f)

        world.update(delta)
    }

    override fun dispose() {
        batch.disposeSafely()
        unloadKoinModules(gameModule)
    }

    override fun resize(
        width: Int,
        height: Int,
    ) {
        viewport.update(width, height)
    }
}
