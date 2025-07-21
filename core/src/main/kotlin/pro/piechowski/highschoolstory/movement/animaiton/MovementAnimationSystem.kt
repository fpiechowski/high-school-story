package pro.piechowski.highschoolstory.movement.animaiton

import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World
import pro.piechowski.highschoolstory.ReadOnly
import pro.piechowski.highschoolstory.Write
import pro.piechowski.highschoolstory.animation.CurrentAnimation
import pro.piechowski.highschoolstory.movement.facedirection.FaceDirection
import pro.piechowski.highschoolstory.movement.velocity.Velocity

class MovementAnimationSystem :
    IteratingSystem(
        World.Companion.family {
            all(
                @ReadOnly Velocity,
                @ReadOnly FaceDirection,
                @Write CurrentAnimation,
            ).any(
                @ReadOnly MovementAnimation.Idle,
                @ReadOnly MovementAnimation.Walk,
            )
        },
    ) {
    override fun onTickEntity(entity: Entity) {
        val velocity = entity[Velocity.Companion].velocity

        val currentAnimation = entity[CurrentAnimation]

        if (velocity.len() > 0) {
            entity[MovementAnimation.Walk]
                .animations[entity[FaceDirection].faceDirection.toDirection4()]
                .let {
                    if (currentAnimation.animation != it) {
                        currentAnimation.animation = it
                        currentAnimation.time = 0f
                    }
                }
        } else {
            entity[MovementAnimation.Idle]
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
