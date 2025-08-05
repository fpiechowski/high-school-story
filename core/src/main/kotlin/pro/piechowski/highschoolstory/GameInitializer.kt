package pro.piechowski.highschoolstory

import com.github.quillraven.fleks.World
import ktx.assets.async.AssetStorage
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import pro.piechowski.highschoolstory.asset.AssetIdentifiers
import pro.piechowski.highschoolstory.character.Character
import pro.piechowski.highschoolstory.character.player.PlayerCharacter
import pro.piechowski.highschoolstory.dialogue.Dialogue
import pro.piechowski.highschoolstory.dialogue.DialogueManager
import pro.piechowski.highschoolstory.dialogue.dialogue
import pro.piechowski.highschoolstory.ecs.plusAssign
import pro.piechowski.highschoolstory.gdx.PhysicsWorld
import pro.piechowski.highschoolstory.interaction.interactable.Interactable
import pro.piechowski.highschoolstory.map.PlaceManager
import pro.piechowski.highschoolstory.map.Tile
import pro.piechowski.highschoolstory.physics.body.PhysicsBody
import pro.piechowski.highschoolstory.physics.px
import pro.piechowski.highschoolstory.physics.times
import pro.piechowski.highschoolstory.place.Town

class GameInitializer : KoinComponent {
    private val world: World by inject()
    private val physicsWorld: PhysicsWorld by inject()
    private val assetStorage: AssetStorage by inject()
    private val placeManager: PlaceManager by inject()
    private val dialogueManager: DialogueManager by inject()

    suspend fun initialize(gameState: GameState) {
        with(world) {
            with(assetStorage) {
                placeManager.openPlace(gameState.currentPlace)
                world.loadSnapshot(gameState.worldSnapshot)
            }
        }
    }

    suspend fun initializeTestGame() {
        placeManager.openPlace(Town)

        with(world) {
            with(physicsWorld) {
                with(assetStorage) {
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
    }
}
