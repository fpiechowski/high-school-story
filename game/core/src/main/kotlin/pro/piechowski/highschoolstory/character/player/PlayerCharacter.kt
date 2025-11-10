package pro.piechowski.highschoolstory.character.player

import com.github.quillraven.fleks.Entity
import kotlinx.serialization.Serializable
import pro.piechowski.highschoolstory.animation.character.player.PlayerCharacterMovementAnimationSet
import pro.piechowski.highschoolstory.physics.body.character.CharacterBody
import pro.piechowski.kge.character.player.PlayerCharacterBase

@Serializable(with = PlayerCharacterBase.Serializer::class)
class PlayerCharacter(
    entity: Entity,
) : PlayerCharacterBase(entity) {
    companion object {
        suspend operator fun invoke(
            firstName: String,
            lastName: String,
        ) = PlayerCharacterBase(
            firstName,
            lastName,
            CharacterBody(),
            PlayerCharacterMovementAnimationSet.Idle(),
            PlayerCharacterMovementAnimationSet.Walk(),
        )
    }
}
