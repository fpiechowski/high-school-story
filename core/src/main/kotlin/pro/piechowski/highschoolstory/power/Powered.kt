package pro.piechowski.highschoolstory.power

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType

class Powered(
    var powered: Boolean = false,
) : Component<Powered> {
    override fun type(): ComponentType<Powered> = Powered

    companion object : ComponentType<Powered>()
}
