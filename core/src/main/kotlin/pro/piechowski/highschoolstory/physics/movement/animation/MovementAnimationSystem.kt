package pro.piechowski.highschoolstory.physics.movement.animation

import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World
import pro.piechowski.highschoolstory.animation.CurrentAnimation
import pro.piechowski.highschoolstory.ecs.ReadOnly
import pro.piechowski.highschoolstory.ecs.Write
import pro.piechowski.highschoolstory.physics.body.PhysicsBody
import pro.piechowski.highschoolstory.physics.movement.facedirection.FaceDirection

class MovementAnimationSystem :
    IteratingSystem(
        World.Companion.family {
            all(
                @ReadOnly PhysicsBody,
                @ReadOnly FaceDirection,
                @Write CurrentAnimation,
            ).any(
                @ReadOnly MovementAnimationSet.Idle,
                @ReadOnly MovementAnimationSet.Walk,
            )
        },
    ) {
    override fun onTickEntity(entity: Entity) {
        val velocity = entity[PhysicsBody].body.linearVelocity

        val currentAnimation = entity[CurrentAnimation]

        if (velocity.len() > 0) {
            entity[MovementAnimationSet.Walk]
                .animations[entity[FaceDirection].faceDirection.toDirection4()]
                .let {
                    if (currentAnimation.animation != it) {
                        currentAnimation.animation = it
                        currentAnimation.time = 0f
                    }
                }
        } else {
            entity[MovementAnimationSet.Idle]
                .animations[entity[FaceDirection].faceDirection.toDirection4()]
                .let {
                    if (currentAnimation.animation != it) {
                        currentAnimation.animation = it
                        currentAnimation.time = 0f
                    }
                }
        }
    }
}
