package pro.piechowski.highschoolstory.physics

import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.World
import ktx.collections.toGdxArray

typealias PhysicsWorld = World

fun PhysicsWorld.removeAll() =
    arrayOf<Body>().toGdxArray().let { bodies ->
        getBodies(bodies)
        bodies.forEach { destroyBody(it) }
    }
