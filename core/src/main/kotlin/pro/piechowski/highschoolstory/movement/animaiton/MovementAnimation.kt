package pro.piechowski.highschoolstory.movement.animaiton

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import pro.piechowski.highschoolstory.animation.Direction4Animations

sealed class MovementAnimation(
    open val animations: Direction4Animations,
) {
    data class Idle(
        override val animations: Direction4Animations,
    ) : MovementAnimation(animations),
        Component<Idle> {
        override fun type(): ComponentType<Idle> = Idle

        companion object : ComponentType<Idle>()
    }

    data class Walk(
        override val animations: Direction4Animations,
    ) : MovementAnimation(animations),
        Component<Walk> {
        override fun type(): ComponentType<Walk> = Walk

        companion object : ComponentType<Walk>()
    }
}
