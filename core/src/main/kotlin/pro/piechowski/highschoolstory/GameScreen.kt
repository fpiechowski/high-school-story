package pro.piechowski.highschoolstory

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.Viewport
import com.github.quillraven.fleks.World
import ktx.app.KtxScreen
import ktx.app.clearScreen
import ktx.assets.async.AssetStorage
import ktx.assets.disposeSafely
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.unloadKoinModules
import org.koin.core.module.Module
import pro.piechowski.highschoolstory.asset.AssetIdentifiers
import pro.piechowski.highschoolstory.character.Character
import pro.piechowski.highschoolstory.character.PlayerCharacter
import pro.piechowski.highschoolstory.dialogue.Dialogue
import pro.piechowski.highschoolstory.dialogue.DialogueManager
import pro.piechowski.highschoolstory.dialogue.dialogue
import pro.piechowski.highschoolstory.ecs.plusAssign
import pro.piechowski.highschoolstory.gdx.PhysicsWorld
import pro.piechowski.highschoolstory.input.GameInputMultiplexer
import pro.piechowski.highschoolstory.input.InputState
import pro.piechowski.highschoolstory.interaction.Interactable
import pro.piechowski.highschoolstory.map.MapManager
import pro.piechowski.highschoolstory.map.Tile
import pro.piechowski.highschoolstory.physics.body.PhysicsBody
import pro.piechowski.highschoolstory.physics.meterCameraQualifier
import pro.piechowski.highschoolstory.physics.meterViewportQualifier
import pro.piechowski.highschoolstory.physics.px
import pro.piechowski.highschoolstory.physics.times
import pro.piechowski.highschoolstory.rendering.pixelCameraQualifier
import pro.piechowski.highschoolstory.rendering.pixelViewportQualifier
import pro.piechowski.highschoolstory.ui.UserInterface
import pro.piechowski.highschoolstory.ui.uiViewportQualifier

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
    private val pixelCamera: Camera by inject(pixelCameraQualifier)
    private val meterCamera: Camera by inject(meterCameraQualifier)
    private val pixelViewport: Viewport by inject(pixelViewportQualifier)
    private val meterViewport: Viewport by inject(meterViewportQualifier)
    private val uiViewport: Viewport by inject(uiViewportQualifier)
    private val world: World by inject()
    private val physicsWorld: PhysicsWorld by inject()
    private val stage: Stage by inject()
    private val dialogueManager: DialogueManager by inject()
    private val userInterface: UserInterface by inject()
    private val mapManager: MapManager by inject()

    init {
        with(world) {
            with(assetStorage) {
                val townMap = get(AssetIdentifiers.Maps.Town)
                mapManager.openMap(townMap)

                with(physicsWorld) {
                    val playerCharacter =
                        entity {
                            it += PlayerCharacter.archetype("Player", "Character")

                            it[PhysicsBody].body.setTransform(Tile.Position(200, 202).toPixel() * px.toMeter(), 0f)
                        }

                    entity {
                        it += Character.archetype("NPC", "", AssetIdentifiers.Textures.Character)
                        it[PhysicsBody].body.setTransform(Tile.Position(213, 202).toPixel() * px.toMeter(), 0f)
                        it += Dialogue.Actor("NPC")
                        it +=
                            Interactable {
                                val npc = it[Dialogue.Actor]
                                val pc = playerCharacter[Dialogue.Actor]
                                dialogueManager.startDialogue(
                                    dialogue {
                                        npc.says(
                                            "Hello!",
                                            id = "hello",
                                            nextNode =
                                                pc.choice {
                                                    option(
                                                        "Hi!",
                                                        onAdvanced = { println("You're kind") },
                                                        nextNode = npc.says("What a nice day!"),
                                                    )
                                                    option(
                                                        "Fuck you!",
                                                        onAdvanced = { println("You're rude") },
                                                        nextNode =
                                                            npc.says(
                                                                "Let's try again",
                                                                nextNode = goTo("hello"),
                                                            ),
                                                    )
                                                    option("Goodbye!")
                                                },
                                        )
                                    },
                                )
                            }
                    }
                }
            }
        }

        userInterface.addActors()
    }

    override fun render(delta: Float) {
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
        pixelViewport.update(width, height)
        meterViewport.update(width, height)
        uiViewport.update(width, height)
    }
}
