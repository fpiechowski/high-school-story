package pro.piechowski.highschoolstory.character

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.BodyDef
import com.github.quillraven.fleks.EntityTag
import kotlinx.serialization.Serializable
import ktx.assets.async.AssetStorage
import ktx.assets.async.Identifier
import ktx.box2d.body
import ktx.box2d.box
import ktx.collections.toGdxArray
import pro.piechowski.highschoolstory.animation.CurrentAnimation
import pro.piechowski.highschoolstory.animation.Direction4Animations
import pro.piechowski.highschoolstory.character.animation.CharacterAnimation
import pro.piechowski.highschoolstory.character.rendering.CharacterSprite
import pro.piechowski.highschoolstory.character.rendering.CharacterTexture
import pro.piechowski.highschoolstory.dialogue.Dialogue
import pro.piechowski.highschoolstory.direction.Direction4
import pro.piechowski.highschoolstory.direction.Direction8
import pro.piechowski.highschoolstory.ecs.Archetype
import pro.piechowski.highschoolstory.gdx.GdxSprite
import pro.piechowski.highschoolstory.gdx.PhysicsWorld
import pro.piechowski.highschoolstory.interaction.Interactor
import pro.piechowski.highschoolstory.interaction.input.InteractionInput
import pro.piechowski.highschoolstory.physics.body.PhysicsBody
import pro.piechowski.highschoolstory.physics.movement.Speed
import pro.piechowski.highschoolstory.physics.movement.animation.MovementAnimation
import pro.piechowski.highschoolstory.physics.movement.facedirection.FaceDirection
import pro.piechowski.highschoolstory.physics.movement.input.MovementInput
import pro.piechowski.highschoolstory.physics.px
import pro.piechowski.highschoolstory.rendering.sprite.CurrentSprite

@Serializable
data object Character : EntityTag() {
    context(assetStorage: AssetStorage, physicsWorld: PhysicsWorld)
    fun archetype(
        firstName: String,
        lastName: String,
        spriteSheetIdentifier: Identifier<Texture>,
    ) = Archetype {
        this += Character

        this +=
            PhysicsBody(
                physicsWorld.body(BodyDef.BodyType.DynamicBody) {
                    box(
                        CharacterSprite.WIDTH.px
                            .toMeter()
                            .value,
                        CharacterSprite.HEIGHT.px
                            .toMeter()
                            .value / HEIGHT_TO_DEPTH_RATIO,
                        bodyFixturePositionOffset,
                    )
                },
            )

        val characterTexture = assetStorage[spriteSheetIdentifier]
        val characterTextureRegions =
            TextureRegion.split(
                characterTexture,
                CharacterSprite.WIDTH.toInt(),
                CharacterSprite.HEIGHT.toInt(),
            )
        val downIdleAnimation =
            Animation(
                CharacterAnimation.duration.value,
                characterTextureRegions[CharacterTexture.getAnimationRegionsRow(CharacterAnimation.IDLE)]
                    .slice(CharacterTexture.getLocomotionAnimationRegionsColumnRange(Direction4.Down))
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
                                CharacterAnimation.duration.value,
                                characterTextureRegions[CharacterTexture.getAnimationRegionsRow(CharacterAnimation.IDLE)]
                                    .slice(CharacterTexture.getLocomotionAnimationRegionsColumnRange(Direction4.Right))
                                    .map { region -> CharacterSprite(region) }
                                    .toGdxArray(),
                                Animation.PlayMode.LOOP,
                            ),
                        up =
                            Animation(
                                CharacterAnimation.duration.value,
                                characterTextureRegions[CharacterTexture.getAnimationRegionsRow(CharacterAnimation.IDLE)]
                                    .slice(CharacterTexture.getLocomotionAnimationRegionsColumnRange(Direction4.Up))
                                    .map { region -> CharacterSprite(region) }
                                    .toGdxArray(),
                                Animation.PlayMode.LOOP,
                            ),
                        left =
                            Animation(
                                CharacterAnimation.duration.value,
                                characterTextureRegions[CharacterTexture.getAnimationRegionsRow(CharacterAnimation.IDLE)]
                                    .slice(CharacterTexture.getLocomotionAnimationRegionsColumnRange(Direction4.Left))
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
                                CharacterAnimation.duration.value,
                                characterTextureRegions[CharacterTexture.getAnimationRegionsRow(CharacterAnimation.WALK)]
                                    .slice(CharacterTexture.getLocomotionAnimationRegionsColumnRange(Direction4.Right))
                                    .map { region -> CharacterSprite(region) }
                                    .toGdxArray(),
                                Animation.PlayMode.LOOP,
                            ),
                        up =
                            Animation(
                                CharacterAnimation.duration.value,
                                characterTextureRegions[CharacterTexture.getAnimationRegionsRow(CharacterAnimation.WALK)]
                                    .slice(CharacterTexture.getLocomotionAnimationRegionsColumnRange(Direction4.Up))
                                    .map { region -> CharacterSprite(region) }
                                    .toGdxArray(),
                                Animation.PlayMode.LOOP,
                            ),
                        left =
                            Animation(
                                CharacterAnimation.duration.value,
                                characterTextureRegions[CharacterTexture.getAnimationRegionsRow(CharacterAnimation.WALK)]
                                    .slice(CharacterTexture.getLocomotionAnimationRegionsColumnRange(Direction4.Left))
                                    .map { region -> CharacterSprite(region) }
                                    .toGdxArray(),
                                Animation.PlayMode.LOOP,
                            ),
                        down =
                            Animation(
                                CharacterAnimation.duration.value,
                                characterTextureRegions[CharacterTexture.getAnimationRegionsRow(CharacterAnimation.WALK)]
                                    .slice(CharacterTexture.getLocomotionAnimationRegionsColumnRange(Direction4.Down))
                                    .map { region -> CharacterSprite(region) }
                                    .toGdxArray(),
                                Animation.PlayMode.LOOP,
                            ),
                    ),
            )
        this +=
            CurrentSprite(
                GdxSprite(
                    characterTextureRegions[
                        CharacterTexture.getAnimationRegionsRow(
                            CharacterAnimation.IDLE,
                        ),
                    ][CharacterTexture.getLocomotionAnimationRegionsColumnRange(Direction4.Down).first()],
                ),
            )
        this += CurrentAnimation(downIdleAnimation)
        this += MovementInput.Multiplex()
        this += Speed.walk
        this += FaceDirection(Direction8.Down)
        this += Interactor
        this += InteractionInput()
        this += Name(firstName, lastName)
        this += Dialogue.Actor(this[Name].fullName)
    }

    const val HEIGHT_TO_DEPTH_RATIO = 4f
    val bodyFixturePositionOffset = Vector2(0f, -(CharacterSprite.HEIGHT / HEIGHT_TO_DEPTH_RATIO).px.toMeter().value)
}
