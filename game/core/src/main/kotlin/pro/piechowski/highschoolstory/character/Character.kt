package pro.piechowski.highschoolstory.character

import com.github.quillraven.fleks.Entity
import pro.piechowski.highschoolstory.animation.character.CharacterAnimation
import pro.piechowski.highschoolstory.physics.body.character.CharacterBody
import pro.piechowski.highschoolstory.sprite.character.CharacterSpriteSheet
import pro.piechowski.kge.animation.Direction4AnimationSet
import pro.piechowski.kge.animation.movement.MovementAnimationSet
import pro.piechowski.kge.character.CharacterBase
import pro.piechowski.kge.ecs.plusAssign
import pro.piechowski.kge.world

class Character(
    entity: Entity,
) : CharacterBase(entity) {
    companion object {
        suspend operator fun invoke(
            firstName: String,
            lastName: String,
            spriteSheet: CharacterSpriteSheet,
        ) = world.entity {
            it +=
                archetype(
                    it,
                    firstName,
                    lastName,
                    CharacterBody(),
                    MovementAnimationSet.Idle(
                        Direction4AnimationSet(
                            CharacterAnimation.Idle.Up(spriteSheet),
                            CharacterAnimation.Idle.Down(spriteSheet),
                            CharacterAnimation.Idle.Left(spriteSheet),
                            CharacterAnimation.Idle.Right(spriteSheet),
                        ),
                    ),
                    MovementAnimationSet.Walk(
                        Direction4AnimationSet(
                            CharacterAnimation.Walk.Up(spriteSheet),
                            CharacterAnimation.Walk.Down(spriteSheet),
                            CharacterAnimation.Walk.Left(spriteSheet),
                            CharacterAnimation.Walk.Right(spriteSheet),
                        ),
                    ),
                )
        }
    }
}
