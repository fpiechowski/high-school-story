package pro.piechowski.highschoolstory.character.player

import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.EntityComponentContext
import com.github.quillraven.fleks.EntityCreateContext
import ktx.assets.async.AssetStorage
import pro.piechowski.highschoolstory.asset.AssetIdentifiers
import pro.piechowski.highschoolstory.character.Character
import pro.piechowski.highschoolstory.ecs.Archetype
import pro.piechowski.highschoolstory.ecs.Archetype.Companion.invoke
import pro.piechowski.highschoolstory.gdx.PhysicsWorld
import pro.piechowski.highschoolstory.physics.body.PhysicsBody
import pro.piechowski.highschoolstory.physics.movement.Speed
import pro.piechowski.highschoolstory.physics.movement.input.MovementInput
import pro.piechowski.highschoolstory.rendering.sprite.CurrentSprite

class PlayerCharacter private constructor(
    val entity: Entity,
) {
    context(ecc: EntityComponentContext)
    val body: PhysicsBody get() = with(ecc) { entity[PhysicsBody.Companion] }

    context(ecc: EntityComponentContext)
    val sprite: CurrentSprite get() = with(ecc) { entity[CurrentSprite.Companion] }

    companion object {
        context(ecc: EntityComponentContext)
        operator fun invoke(entity: Entity) =
            PlayerCharacter(entity).apply {
                with(ecc) {
                    requireNotNull(body)
                    requireNotNull(sprite)
                }
            }

        context(ecc: EntityCreateContext, assetStorage: AssetStorage, physicsWorld: PhysicsWorld)
        fun archetype(
            firstName: String,
            lastName: String,
        ) = Archetype {
            this += Character.archetype(firstName, lastName, AssetIdentifiers.Textures.PlayerCharacter)
            this += Speed.run
            this += PlayerCharacterTag
            this += MovementInput.Controller()
        }
    }
}
