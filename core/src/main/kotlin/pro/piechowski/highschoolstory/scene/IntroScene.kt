package pro.piechowski.highschoolstory.scene

import com.github.quillraven.fleks.World
import kotlinx.coroutines.launch
import ktx.async.KtxAsync
import ktx.async.onRenderingThread
import ktx.box2d.body
import ktx.box2d.box
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import pro.piechowski.highschoolstory.camera.CameraSet
import pro.piechowski.highschoolstory.exterior.ExteriorTexture
import pro.piechowski.highschoolstory.gdx.PhysicsWorld
import pro.piechowski.highschoolstory.physics.body.PhysicsBody
import pro.piechowski.highschoolstory.physics.px
import pro.piechowski.highschoolstory.place.PlaceManager
import pro.piechowski.highschoolstory.place.Road
import pro.piechowski.highschoolstory.rendering.sprite.CurrentSprite
import pro.piechowski.highschoolstory.vehicle.bus.BusSprite

class IntroScene :
    Scene(),
    KoinComponent {
    private val placeManager by inject<PlaceManager>()
    private val cameraSet by inject<CameraSet>()

    private val world by inject<World>()
    private val physicsWorld by inject<PhysicsWorld>()
    private val exteriorTexture by inject<ExteriorTexture>()

    override suspend fun play() =
        KtxAsync.launch {
            placeManager.openPlace(Road)
            cameraSet.moveTo(720f.px, 480f.px)

            world.entity {
                it += CurrentSprite(BusSprite.Brown.Right(exteriorTexture))
                onRenderingThread {
                    it +=
                        PhysicsBody(
                            physicsWorld.body {
                                box(336f.px.toMeter().value, 192f.px.toMeter().value)
                            },
                        )
                }
            }
        }
}
