package pro.piechowski.highschoolstory.character

import com.badlogic.gdx.graphics.Texture
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.EntityComponentContext
import kotlinx.serialization.Serializable
import ktx.assets.async.AssetStorage
import ktx.assets.async.Identifier
import org.koin.core.Koin
import pro.piechowski.highschoolstory.Name
import pro.piechowski.highschoolstory.animation.CurrentAnimation
import pro.piechowski.highschoolstory.animation.Direction4AnimationSet
import pro.piechowski.highschoolstory.animation.character.CharacterAnimation
import pro.piechowski.highschoolstory.animation.movement.MovementAnimationSet
import pro.piechowski.highschoolstory.dialogue.Dialogue
import pro.piechowski.highschoolstory.dialogue.DialogueBuilder
import pro.piechowski.highschoolstory.direction.Direction8
import pro.piechowski.highschoolstory.ecs.Archetype
import pro.piechowski.highschoolstory.ecs.get
import pro.piechowski.highschoolstory.facedirection.FaceDirection8
import pro.piechowski.highschoolstory.gdx.PhysicsWorld
import pro.piechowski.highschoolstory.get
import pro.piechowski.highschoolstory.input.interaction.InteractionInput
import pro.piechowski.highschoolstory.input.movement.MovementInput
import pro.piechowski.highschoolstory.interaction.interactor.InteractorTag
import pro.piechowski.highschoolstory.movement.Speed
import pro.piechowski.highschoolstory.`object`.Kinetic
import pro.piechowski.highschoolstory.`object`.Spatial
import pro.piechowski.highschoolstory.`object`.Visual
import pro.piechowski.highschoolstory.physics.body.PhysicsBody
import pro.piechowski.highschoolstory.physics.body.character.CharacterBody
import pro.piechowski.highschoolstory.sprite.CurrentSprite

@Serializable
open class CharacterBase(
    override val entity: Entity,
) : Visual,
    Kinetic,
    Spatial {
    context(_: EntityComponentContext)
    val dialogueActor: Dialogue.Actor get() = entity[Dialogue.Actor]

    companion object {
        context(_: Koin)
        suspend fun archetype(
            firstName: String,
            lastName: String,
            spriteSheetIdentifier: Identifier<Texture>,
        ) = with(get<PhysicsWorld>()) {
            Archetype {
                this += CharacterTag

                this +=
                    PhysicsBody(CharacterBody())

                val characterTexture = get<AssetStorage>().load(spriteSheetIdentifier)
                val downIdleAnimation = CharacterAnimation.Idle.Down(characterTexture)
                this +=
                    MovementAnimationSet.Idle(
                        animations =
                            Direction4AnimationSet(
                                right = CharacterAnimation.Idle.Right(characterTexture),
                                up = CharacterAnimation.Idle.Up(characterTexture),
                                left = CharacterAnimation.Idle.Left(characterTexture),
                                down = downIdleAnimation,
                            ),
                    )
                this +=
                    MovementAnimationSet.Walk(
                        animations =
                            Direction4AnimationSet(
                                right = CharacterAnimation.Walk.Right(characterTexture),
                                up = CharacterAnimation.Walk.Up(characterTexture),
                                left = CharacterAnimation.Walk.Left(characterTexture),
                                down = CharacterAnimation.Walk.Down(characterTexture),
                            ),
                    )
                this += CurrentSprite(downIdleAnimation.keyFrames.first())
                this += CurrentAnimation(downIdleAnimation)
                this += MovementInput.Multiplex()
                this += Speed.walk
                this += FaceDirection8(Direction8.Down)
                this += InteractorTag
                this += InteractionInput()
                this += Name(firstName, lastName)
                this += Dialogue.Actor(this[Name].fullName)
            }
        }

        const val HEIGHT_TO_DEPTH_RATIO = 4f
    }
}

context(ecc: EntityComponentContext, dialogueBuilder: DialogueBuilder)
fun CharacterBase.says(
    line: String,
    id: String? = null,
    onAdvanced: () -> Unit = {
    },
    nextNode: Dialogue.Node = Dialogue.Node.End,
) = with(dialogueBuilder) { dialogueActor.says(line, id, onAdvanced, nextNode) }
