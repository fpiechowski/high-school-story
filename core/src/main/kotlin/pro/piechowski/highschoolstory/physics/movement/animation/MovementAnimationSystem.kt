package pro.piechowski.highschoolstory.physics.movement.animation

import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World
import pro.piechowski.highschoolstory.animation.CurrentAnimation
import pro.piechowski.highschoolstory.ecs.ReadOnly
import pro.piechowski.highschoolstory.ecs.Write
import pro.piechowski.highschoolstory.physics.body.PhysicsBody
import pro.piechowski.highschoolstory.physics.movement.facedirection.FaceDirection
import pro.piechowski.highschoolstory.physics.movement.facedirection.FaceDirection4
import pro.piechowski.highschoolstory.physics.movement.facedirection.FaceDirection8
import pro.piechowski.highschoolstory.physics.movement.facedirection.get

class MovementAnimationSystem :
    IteratingSystem(
        World.Companion.family {
            all(
                @ReadOnly PhysicsBody,
                @Write CurrentAnimation,
            ).any(
                @ReadOnly MovementAnimationSet.Idle,
                @ReadOnly MovementAnimationSet.Walk,
            ).any(
                @ReadOnly FaceDirection4,
                @ReadOnly FaceDirection8,
            )
        },
    ) {
    override fun onTickEntity(entity: Entity) {
        val velocity = entity[PhysicsBody].body.linearVelocity

        val currentAnimation = entity[CurrentAnimation]

        val direction4 =
            when (entity[FaceDirection]) {
                is FaceDirection4 -> entity[FaceDirection4].faceDirection
                is FaceDirection8 -> entity[FaceDirection8].faceDirection.toDirection4()
            }

        if (velocity.len() > 0) {
            entity[MovementAnimationSet.Walk]
                .animations[direction4]
                .let {
                    if (currentAnimation.animation != it) {
                        currentAnimation.animation = it
                        currentAnimation.time = 0f
                    }
                }
        } else {
            entity[MovementAnimationSet.Idle]
                .animations[direction4]
                .let {
                    if (currentAnimation.animation != it) {
                        currentAnimation.animation = it
                        currentAnimation.time = 0f
                    }
                }
        }
    }
}
