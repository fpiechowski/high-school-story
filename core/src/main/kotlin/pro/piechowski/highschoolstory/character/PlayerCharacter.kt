package pro.piechowski.highschoolstory.character

import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.EntityComponentContext
import com.github.quillraven.fleks.EntityCreateContext
import com.github.quillraven.fleks.EntityTag
import kotlinx.serialization.Serializable
import ktx.assets.async.AssetStorage
import pro.piechowski.highschoolstory.asset.AssetIdentifiers
import pro.piechowski.highschoolstory.ecs.Archetype
import pro.piechowski.highschoolstory.gdx.PhysicsWorld
import pro.piechowski.highschoolstory.physics.body.PhysicsBody
import pro.piechowski.highschoolstory.physics.movement.Speed
import pro.piechowski.highschoolstory.physics.movement.input.MovementInput
import pro.piechowski.highschoolstory.rendering.sprite.CurrentSprite

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

class PlayerCharacterEntity private constructor(
    val entity: Entity,
) {
    context(ecc: EntityComponentContext)
    val body: PhysicsBody get() = with(ecc) { entity[PhysicsBody] }

    context(ecc: EntityComponentContext)
    val sprite: CurrentSprite get() = with(ecc) { entity[CurrentSprite] }

    companion object {
        context(ecc: EntityComponentContext)
        operator fun invoke(entity: Entity) =
            PlayerCharacterEntity(entity).apply {
                with(ecc) {
                    requireNotNull(body)
                    requireNotNull(sprite)
                }
            }
    }
}
