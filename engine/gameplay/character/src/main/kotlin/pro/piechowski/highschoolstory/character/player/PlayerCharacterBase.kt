package pro.piechowski.highschoolstory.character.player

import com.badlogic.gdx.graphics.Texture
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.World
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import ktx.assets.async.Identifier
import org.koin.core.Koin
import pro.piechowski.highschoolstory.character.CharacterBase
import pro.piechowski.highschoolstory.ecs.Archetype
import pro.piechowski.highschoolstory.ecs.plusAssign
import pro.piechowski.highschoolstory.input.InputOwner
import pro.piechowski.highschoolstory.input.movement.MovementInput
import pro.piechowski.highschoolstory.movement.Speed

@Serializable(with = PlayerCharacterBase.Serializer::class)
open class PlayerCharacterBase private constructor(
    entity: Entity,
) : CharacterBase(entity),
    InputOwner {
    companion object {
        context(koin: Koin)
        suspend operator fun invoke(
            firstName: String,
            lastName: String,
            spriteIdentifier: Identifier<Texture>,
        ) = PlayerCharacterBase(
            koin.get<World>().entity {
                it += archetype(firstName, lastName, spriteIdentifier)
            },
        ).also {
            koin.get<PlayerCharacterManager>().playerCharacter.value = it
        }

        context(koin: Koin)
        suspend fun archetype(
            firstName: String,
            lastName: String,
            spriteIdentifier: Identifier<Texture>,
        ) = Archetype {
            this += CharacterBase.archetype(firstName, lastName, spriteIdentifier)
            this += Speed.run
            this += PlayerCharacterTag
            this += MovementInput.Controller()
        }
    }

    object Serializer : KSerializer<PlayerCharacterBase> {
        override val descriptor = CharacterBase.serializer().descriptor

        override fun serialize(
            encoder: Encoder,
            value: PlayerCharacterBase,
        ) {
            CharacterBase.serializer().serialize(encoder, value)
        }

        override fun deserialize(decoder: Decoder): PlayerCharacterBase {
            val character = CharacterBase.serializer().deserialize(decoder)
            return PlayerCharacterBase(character.entity)
        }
    }
}
