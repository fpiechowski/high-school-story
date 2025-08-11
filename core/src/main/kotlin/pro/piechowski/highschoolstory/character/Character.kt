package pro.piechowski.highschoolstory.character

import com.badlogic.gdx.graphics.Texture
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.EntityComponentContext
import kotlinx.serialization.Serializable
import ktx.assets.async.AssetStorage
import ktx.assets.async.Identifier
import org.koin.core.Koin
import pro.piechowski.highschoolstory.animation.CurrentAnimation
import pro.piechowski.highschoolstory.animation.Direction4AnimationSet
import pro.piechowski.highschoolstory.character.animation.CharacterAnimation
import pro.piechowski.highschoolstory.character.body.CharacterBody
import pro.piechowski.highschoolstory.dialogue.Dialogue
import pro.piechowski.highschoolstory.dialogue.DialogueBuilder
import pro.piechowski.highschoolstory.direction.Direction8
import pro.piechowski.highschoolstory.ecs.Archetype
import pro.piechowski.highschoolstory.gdx.PhysicsWorld
import pro.piechowski.highschoolstory.interaction.input.InteractionInput
import pro.piechowski.highschoolstory.interaction.interactor.Interactor
import pro.piechowski.highschoolstory.physics.body.PhysicsBody
import pro.piechowski.highschoolstory.physics.movement.Speed
import pro.piechowski.highschoolstory.physics.movement.animation.MovementAnimationSet
import pro.piechowski.highschoolstory.physics.movement.facedirection.FaceDirection
import pro.piechowski.highschoolstory.physics.movement.facedirection.FaceDirection8
import pro.piechowski.highschoolstory.physics.movement.input.MovementInput
import pro.piechowski.highschoolstory.spatial.Spatial
import pro.piechowski.highschoolstory.sprite.CurrentSprite

@Serializable
open class Character(
    override val entity: Entity,
) : Spatial {
    context(ecc: EntityComponentContext)
    val dialogueActor: Dialogue.Actor get() = with(ecc) { entity[Dialogue.Actor] }

    companion object {
        context(koin: Koin)
        suspend fun archetype(
            firstName: String,
            lastName: String,
            spriteSheetIdentifier: Identifier<Texture>,
        ) = with(koin) {
            with(get<PhysicsWorld>()) {
                Archetype {
                    this += CharacterTag

                    this +=
                        PhysicsBody(CharacterBody())

                    val characterTexture = get<AssetStorage>()[spriteSheetIdentifier]
                    val downIdleAnimation =
                        CharacterAnimation.Idle.Down(characterTexture)
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
                    this += Interactor
                    this += InteractionInput()
                    this += Name(firstName, lastName)
                    this += Dialogue.Actor(this[Name].fullName)
                }
            }
        }

        const val HEIGHT_TO_DEPTH_RATIO = 4f
    }
}

context(ecc: EntityComponentContext, dialogueBuilder: DialogueBuilder)
fun Character.says(
    line: String,
    id: String? = null,
    onAdvanced: () -> Unit = {
    },
    nextNode: Dialogue.Node = Dialogue.Node.End,
) = with(dialogueBuilder) { dialogueActor.says(line, id, onAdvanced, nextNode) }
