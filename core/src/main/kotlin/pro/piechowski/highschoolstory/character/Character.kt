package pro.piechowski.highschoolstory.character

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.github.quillraven.fleks.EntityTag
import kotlinx.serialization.Serializable
import ktx.assets.async.AssetStorage
import ktx.assets.async.Identifier
import ktx.collections.toGdxArray
import pro.piechowski.highschoolstory.Archetype
import pro.piechowski.highschoolstory.GdxSprite
import pro.piechowski.highschoolstory.animation.CurrentAnimation
import pro.piechowski.highschoolstory.animation.Direction4Animations
import pro.piechowski.highschoolstory.direction.Direction8
import pro.piechowski.highschoolstory.interaction.Interactor
import pro.piechowski.highschoolstory.interaction.input.InteractionInput
import pro.piechowski.highschoolstory.movement.Speed
import pro.piechowski.highschoolstory.movement.animaiton.MovementAnimation
import pro.piechowski.highschoolstory.movement.facedirection.FaceDirection
import pro.piechowski.highschoolstory.movement.input.MovementInput
import pro.piechowski.highschoolstory.movement.position.Position
import pro.piechowski.highschoolstory.movement.velocity.Velocity
import pro.piechowski.highschoolstory.sprite.CharacterSprite
import pro.piechowski.highschoolstory.sprite.CurrentSprite

const val CHARACTER_ANIMATION_DURATION = 1f / 5f

@Serializable
data object Character : EntityTag() {
    context(assetStorage: AssetStorage)
    fun archetype(spriteSheetIdentifier: Identifier<Texture>) =
        Archetype {
            this += Character
            this += Velocity()

            val characterTexture = assetStorage[spriteSheetIdentifier]
            val characterTextureRegions = TextureRegion.split(characterTexture, 48, 96)
            val downIdleAnimation =
                Animation(
                    CHARACTER_ANIMATION_DURATION,
                    characterTextureRegions[1]
                        .slice(18..23)
                        .map { CharacterSprite(it) }
                        .toGdxArray(),
                    Animation.PlayMode.LOOP,
                )
            this +=
                MovementAnimation.Idle(
                    animations =
                        Direction4Animations(
                            right =
                                Animation(
                                    CHARACTER_ANIMATION_DURATION,
                                    characterTextureRegions[1]
                                        .slice(0..5)
                                        .map { region -> CharacterSprite(region) }
                                        .toGdxArray(),
                                    Animation.PlayMode.LOOP,
                                ),
                            up =
                                Animation(
                                    CHARACTER_ANIMATION_DURATION,
                                    characterTextureRegions[1]
                                        .slice(6..11)
                                        .map { region -> CharacterSprite(region) }
                                        .toGdxArray(),
                                    Animation.PlayMode.LOOP,
                                ),
                            left =
                                Animation(
                                    CHARACTER_ANIMATION_DURATION,
                                    characterTextureRegions[1]
                                        .slice(12..17)
                                        .map { region -> CharacterSprite(region) }
                                        .toGdxArray(),
                                    Animation.PlayMode.LOOP,
                                ),
                            down = downIdleAnimation,
                        ),
                )
            this +=
                MovementAnimation.Walk(
                    animations =
                        Direction4Animations(
                            right =
                                Animation(
                                    CHARACTER_ANIMATION_DURATION,
                                    characterTextureRegions[2]
                                        .slice(0..5)
                                        .map { region -> CharacterSprite(region) }
                                        .toGdxArray(),
                                    Animation.PlayMode.LOOP,
                                ),
                            up =
                                Animation(
                                    CHARACTER_ANIMATION_DURATION,
                                    characterTextureRegions[2]
                                        .slice(6..11)
                                        .map { region -> CharacterSprite(region) }
                                        .toGdxArray(),
                                    Animation.PlayMode.LOOP,
                                ),
                            left =
                                Animation(
                                    CHARACTER_ANIMATION_DURATION,
                                    characterTextureRegions[2]
                                        .slice(12..17)
                                        .map { region -> CharacterSprite(region) }
                                        .toGdxArray(),
                                    Animation.PlayMode.LOOP,
                                ),
                            down =
                                Animation(
                                    CHARACTER_ANIMATION_DURATION,
                                    characterTextureRegions[2]
                                        .slice(18..23)
                                        .map { region -> CharacterSprite(region) }
                                        .toGdxArray(),
                                    Animation.PlayMode.LOOP,
                                ),
                        ),
                )
            this += CurrentSprite(GdxSprite(characterTextureRegions[0][3]))
            this += CurrentAnimation(downIdleAnimation)
            this += MovementInput.Multiplex()
            this += Speed(300f)
            this += Position(Vector2.Zero.cpy())
            this += FaceDirection(Direction8.Down)
            this += Interactor
            this += InteractionInput()
        }
}
