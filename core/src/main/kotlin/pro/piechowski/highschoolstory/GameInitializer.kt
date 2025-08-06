package pro.piechowski.highschoolstory

import com.github.quillraven.fleks.World
import ktx.assets.async.AssetStorage
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import pro.piechowski.highschoolstory.camera.CameraSet
import pro.piechowski.highschoolstory.camera.PixelCamera
import pro.piechowski.highschoolstory.dialogue.DialogueManager
import pro.piechowski.highschoolstory.gdx.PhysicsWorld
import pro.piechowski.highschoolstory.physics.px
import pro.piechowski.highschoolstory.place.PlaceManager
import pro.piechowski.highschoolstory.place.Road
import pro.piechowski.highschoolstory.scene.IntroScene

class GameInitializer : KoinComponent {
    private val world: World by inject()
    private val physicsWorld: PhysicsWorld by inject()
    private val assetStorage: AssetStorage by inject()
    private val placeManager: PlaceManager by inject()
    private val dialogueManager: DialogueManager by inject()
    private val cameraSet by inject<CameraSet>()

    suspend fun initialize(gameState: GameState) {
        with(world) {
            with(assetStorage) {
                placeManager.openPlace(gameState.currentPlace)
                world.loadSnapshot(gameState.worldSnapshot)
            }
        }
    }

    private val introScene by inject<IntroScene>()

    suspend fun initializeTestGame() {
        introScene.play()
    }
}
