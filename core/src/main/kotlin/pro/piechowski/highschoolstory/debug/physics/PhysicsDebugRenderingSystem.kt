package pro.piechowski.highschoolstory.debug.physics

import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer
import com.github.quillraven.fleks.Fixed
import com.github.quillraven.fleks.IntervalSystem
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import pro.piechowski.highschoolstory.camera.MeterCamera
import pro.piechowski.highschoolstory.gdx.PhysicsWorld
import pro.piechowski.highschoolstory.physics.body.PHYSICS_STEP

class PhysicsDebugRenderingSystem :
    IntervalSystem(Fixed(PHYSICS_STEP)),
    KoinComponent {
    private val box2DDebugRenderer: Box2DDebugRenderer by inject()
    private val physicsWorld: PhysicsWorld by inject()
    private val meterCamera: MeterCamera by inject()

    override fun onTick() {
        box2DDebugRenderer.render(physicsWorld, meterCamera.combined)
    }
}
