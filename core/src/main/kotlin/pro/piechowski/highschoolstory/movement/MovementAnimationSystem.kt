package pro.piechowski.highschoolstory.movement

import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World
import pro.piechowski.highschoolstory.animation.CurrentAnimation
import pro.piechowski.highschoolstory.direction.FaceDirection
import pro.piechowski.highschoolstory.movement.Velocity

class MovementAnimationSystem :
    IteratingSystem(
        World.Companion.family {
            all(
                CurrentAnimation,
                Velocity.Companion,
                FaceDirection,
            ).any(
                MovementAnimation.Idle,
                MovementAnimation.Walk,
            )
        },
    ) {
    override fun onTickEntity(entity: Entity) {
        val velocity = entity[Velocity.Companion].velocity

        val currentAnimation = entity[CurrentAnimation]

        if (velocity.len() > 0) {
            entity[MovementAnimation.Walk]
                .animations[entity[FaceDirection].faceDirection]
                .let {
                    if (currentAnimation.animation != it) {
                        currentAnimation.animation = it
                        currentAnimation.time = 0f
                    }
                }
        } else {
            entity[MovementAnimation.Idle]
                .animations[entity[FaceDirection].faceDirection]
                .let {
                    if (currentAnimation.animation != it) {
                        currentAnimation.animation = it
                        currentAnimation.time = 0f
                    }
                }
        }
    }
}
