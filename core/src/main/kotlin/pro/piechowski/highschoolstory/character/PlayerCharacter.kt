package pro.piechowski.highschoolstory.character

import com.github.quillraven.fleks.EntityCreateContext
import com.github.quillraven.fleks.EntityTag
import kotlinx.serialization.Serializable
import ktx.assets.async.AssetStorage
import pro.piechowski.highschoolstory.Archetype
import pro.piechowski.highschoolstory.AssetIdentifiers
import pro.piechowski.highschoolstory.movement.input.MovementInput

@Serializable
data object PlayerCharacter : EntityTag() {
    context(ecc: EntityCreateContext, assetStorage: AssetStorage)
    fun archetype() =
        Archetype {
            this += Character.archetype(AssetIdentifiers.Textures.PlayerCharacter)

            this += PlayerCharacter
            this += MovementInput.Controller()
        }
}
