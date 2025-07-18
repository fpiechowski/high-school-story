package pro.piechowski.highschoolstory.sprite

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import pro.piechowski.highschoolstory.GdxSprite

class CurrentSprite(
    var sprite: GdxSprite
) : Component<CurrentSprite> {
    override fun type() = CurrentSprite

    companion object : ComponentType<CurrentSprite>()
}
