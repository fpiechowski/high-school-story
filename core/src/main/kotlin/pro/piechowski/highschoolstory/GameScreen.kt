package pro.piechowski.highschoolstory

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
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
import pro.piechowski.highschoolstory.physics.body.PhysicsBody
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
        assetStorage.loadSync<Texture>(AssetIdentifiers.Textures.PlayerCharacter)
        assetStorage.loadSync<Texture>(AssetIdentifiers.Textures.Character)
    }

    private val batch: SpriteBatch by inject()
    private val camera: Camera by inject(pixelCameraQualifier)
    private val pixelViewport: Viewport by inject(pixelViewportQualifier)
    private val meterViewport: Viewport by inject(meterViewportQualifier)
    private val uiViewport: Viewport by inject(uiViewportQualifier)
    private val world: World by inject()
    private val physicsWorld: PhysicsWorld by inject()
    private val stage: Stage by inject()
    private val dialogueManager: DialogueManager by inject()
    private val userInterface: UserInterface by inject()

    init {
        with(world) {
            with(assetStorage) {
                with(physicsWorld) {
                    val playerCharacter =
                        entity {
                            it += PlayerCharacter.archetype("Player", "Character")
                        }

                    entity {
                        it += Character.archetype("NPC", "", AssetIdentifiers.Textures.Character)
                        it[PhysicsBody].body.setTransform(Vector2(300f, 100f) * px.toMeter(), 0f)
                        it += Dialogue.Actor("NPC")
                        it +=
                            Interactable {
                                dialogueManager.startDialogue(
                                    dialogue {
                                        val npcActor = it[Dialogue.Actor]
                                        val pcActor = playerCharacter[Dialogue.Actor]

                                        npcActor.says("Hey!\nHello there!") {
                                            pcActor.says("Hello!") {
                                                npcActor.says("Nice to meet you.")
                                            }
                                            pcActor.says("Screw you!") {
                                                npcActor.says("That's rude!")
                                            }
                                            pcActor.says("Placeholder 1")
                                            pcActor.says("Placeholder 2")
                                            pcActor.says("Placeholder 3")
                                            pcActor.says("Placeholder 4")
                                            pcActor.says("Placeholder 5")
                                        }
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
        batch.projectionMatrix = camera.combined

        camera.update()

        clearScreen(red = 0.7f, green = 0.7f, blue = 0.7f)

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
