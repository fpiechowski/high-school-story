package pro.piechowski.highschoolstory.physics.movement.animation

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import pro.piechowski.highschoolstory.animation.Direction4AnimationSet

sealed class MovementAnimationSet(
    open val animations: Direction4AnimationSet,
) {
    data class Idle(
        override val animations: Direction4AnimationSet,
    ) : MovementAnimationSet(animations),
        Component<Idle> {
        override fun type(): ComponentType<Idle> = Idle

        companion object : ComponentType<Idle>()
    }

    data class Walk(
        override val animations: Direction4AnimationSet,
    ) : MovementAnimationSet(animations),
        Component<Walk> {
        override fun type(): ComponentType<Walk> = Walk

        companion object : ComponentType<Walk>()
    }
}
