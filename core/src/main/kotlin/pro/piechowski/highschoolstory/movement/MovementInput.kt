package pro.piechowski.highschoolstory.movement

import com.badlogic.gdx.math.Vector2
import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType

sealed class MovementInput(
    open var movementInput: Vector2 = Vector2.Zero.cpy(),
) {
    data class Controller(
        override var movementInput: Vector2 = Vector2.Zero.cpy(),
    ) : MovementInput(),
        Component<Controller> {
        override fun type(): ComponentType<Controller> = Controller

        companion object : ComponentType<Controller>()
    }

    data class AI(
        override var movementInput: Vector2 = Vector2.Zero.cpy(),
    ) : MovementInput(),
        Component<AI> {
        override fun type(): ComponentType<AI> = AI

        companion object : ComponentType<AI>()
    }

    data class Multiplex(
        override var movementInput: Vector2 = Vector2.Zero.cpy(),
    ) : MovementInput(),
        Component<Multiplex> {
        override fun type(): ComponentType<Multiplex> = Multiplex

        companion object : ComponentType<Multiplex>()
    }
}
