package pro.piechowski.highschoolstory.character

import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.github.quillraven.fleks.EntityTag
import com.github.quillraven.fleks.World
import kotlinx.serialization.Serializable
import ktx.assets.async.AssetStorage
import ktx.collections.toGdxArray
import pro.piechowski.highschoolstory.AssetIdentifiers
import pro.piechowski.highschoolstory.GdxSprite
import pro.piechowski.highschoolstory.animation.CurrentAnimation
import pro.piechowski.highschoolstory.animation.DirectionAnimations
import pro.piechowski.highschoolstory.direction.Direction
import pro.piechowski.highschoolstory.direction.FaceDirection
import pro.piechowski.highschoolstory.movement.MovementAnimation
import pro.piechowski.highschoolstory.movement.MovementInput
import pro.piechowski.highschoolstory.movement.Position
import pro.piechowski.highschoolstory.movement.Speed
import pro.piechowski.highschoolstory.movement.Velocity
import pro.piechowski.highschoolstory.sprite.CurrentSprite

const val PLAYER_CHARACTER_ANIMATION_DURATION = 1f / 5f

@Serializable
data object PlayerCharacter : EntityTag() {
    object Factory {
        context(world: World, assetStorage: AssetStorage)
        fun create() =
            world.entity {
                it += PlayerCharacter
                it += Velocity()

                val playerCharacterTexture = assetStorage[AssetIdentifiers.Textures.PlayerCharacter]
                val playerCharacterTextureRegions = TextureRegion.split(playerCharacterTexture, 48, 96)
                val downIdleAnimation =
                    Animation(
                        PLAYER_CHARACTER_ANIMATION_DURATION,
                        playerCharacterTextureRegions[1].slice(18..23).map { GdxSprite(it) }.toGdxArray(),
                        Animation.PlayMode.LOOP,
                    )
                it +=
                    MovementAnimation.Idle(
                        animations =
                            DirectionAnimations(
                                right =
                                    Animation(
                                        PLAYER_CHARACTER_ANIMATION_DURATION,
                                        playerCharacterTextureRegions[1]
                                            .slice(0..5)
                                            .map { region -> GdxSprite(region) }
                                            .toGdxArray(),
                                        Animation.PlayMode.LOOP,
                                    ),
                                up =
                                    Animation(
                                        PLAYER_CHARACTER_ANIMATION_DURATION,
                                        playerCharacterTextureRegions[1]
                                            .slice(6..11)
                                            .map { region -> GdxSprite(region) }
                                            .toGdxArray(),
                                        Animation.PlayMode.LOOP,
                                    ),
                                left =
                                    Animation(
                                        PLAYER_CHARACTER_ANIMATION_DURATION,
                                        playerCharacterTextureRegions[1]
                                            .slice(12..17)
                                            .map { region -> GdxSprite(region) }
                                            .toGdxArray(),
                                        Animation.PlayMode.LOOP,
                                    ),
                                down = downIdleAnimation,
                            ),
                    )
                it +=
                    MovementAnimation.Walk(
                        animations =
                            DirectionAnimations(
                                right =
                                    Animation(
                                        PLAYER_CHARACTER_ANIMATION_DURATION,
                                        playerCharacterTextureRegions[2]
                                            .slice(0..5)
                                            .map { region -> GdxSprite(region) }
                                            .toGdxArray(),
                                        Animation.PlayMode.LOOP,
                                    ),
                                up =
                                    Animation(
                                        PLAYER_CHARACTER_ANIMATION_DURATION,
                                        playerCharacterTextureRegions[2]
                                            .slice(6..11)
                                            .map { region -> GdxSprite(region) }
                                            .toGdxArray(),
                                        Animation.PlayMode.LOOP,
                                    ),
                                left =
                                    Animation(
                                        PLAYER_CHARACTER_ANIMATION_DURATION,
                                        playerCharacterTextureRegions[2]
                                            .slice(12..17)
                                            .map { region -> GdxSprite(region) }
                                            .toGdxArray(),
                                        Animation.PlayMode.LOOP,
                                    ),
                                down =
                                    Animation(
                                        PLAYER_CHARACTER_ANIMATION_DURATION,
                                        playerCharacterTextureRegions[2]
                                            .slice(18..23)
                                            .map { region -> GdxSprite(region) }
                                            .toGdxArray(),
                                        Animation.PlayMode.LOOP,
                                    ),
                            ),
                    )
                it += CurrentSprite(GdxSprite(playerCharacterTextureRegions[0][3]))
                it += CurrentAnimation(downIdleAnimation)
                it += MovementInput.Controller()
                it += MovementInput.Multiplex()
                it += Speed(300f)
                it += Position(Vector2.Zero.cpy())
                it += FaceDirection(Direction.Down)
            }
    }
}
