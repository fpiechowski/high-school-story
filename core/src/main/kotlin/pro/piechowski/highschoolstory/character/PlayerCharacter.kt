package pro.piechowski.highschoolstory.character

import com.github.quillraven.fleks.EntityCreateContext
import com.github.quillraven.fleks.EntityTag
import kotlinx.serialization.Serializable
import ktx.assets.async.AssetStorage
import pro.piechowski.highschoolstory.asset.AssetIdentifiers
import pro.piechowski.highschoolstory.ecs.Archetype
import pro.piechowski.highschoolstory.gdx.PhysicsWorld
import pro.piechowski.highschoolstory.physics.movement.Speed
import pro.piechowski.highschoolstory.physics.movement.input.MovementInput

@Serializable
data object PlayerCharacter : EntityTag() {
    context(ecc: EntityCreateContext, assetStorage: AssetStorage, physicsWorld: PhysicsWorld)
    fun archetype(
        firstName: String,
        lastName: String,
    ) = Archetype {
        this += Character.archetype(firstName, lastName, AssetIdentifiers.Textures.PlayerCharacter)
        this += Speed.run
        this += PlayerCharacter
        this += MovementInput.Controller()
    }
}
