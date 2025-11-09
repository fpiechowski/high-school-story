package pro.piechowski.highschoolstory.input.interaction

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType

class InteractionInput(
    var interacting: Boolean = false,
) : Component<InteractionInput> {
    override fun type() = InteractionInput

    companion object : ComponentType<InteractionInput>()
}
