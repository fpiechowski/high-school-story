package pro.piechowski.highschoolstory.character

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType

data class Name(
    val firstName: String,
    val lastName: String,
) : Component<Name> {
    val fullName get() = "$firstName $lastName"

    override fun type(): ComponentType<Name> = Name

    companion object : ComponentType<Name>()
}
