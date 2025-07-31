package pro.piechowski.highschoolstory.physics

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer
import org.koin.dsl.module
import pro.piechowski.highschoolstory.gdx.PhysicsWorld
import pro.piechowski.highschoolstory.physics.body.PhysicsDebugRenderingSystem
import pro.piechowski.highschoolstory.physics.body.PhysicsWorldStepSystem

val PhysicsModule =
    module {
        single { PhysicsWorld(Vector2.Zero.cpy(), true) }
        single { PhysicsWorldStepSystem() }
        single { PhysicsDebugRenderingSystem() }
        single { Box2DDebugRenderer() }
    }
