package pro.piechowski.highschoolstory.animation.character.player

import pro.piechowski.highschoolstory.animation.character.CharacterAnimation
import pro.piechowski.highschoolstory.sprite.character.player.PlayerCharacterSpriteSheet
import pro.piechowski.kge.animation.Direction4AnimationSet
import pro.piechowski.kge.di.DependencyInjection.Global.get
import pro.piechowski.kge.movement.MovementAnimationSet
import pro.piechowski.kge.koin

object PlayerCharacterMovementAnimationSet {
    class Idle :
        MovementAnimationSet.Idle(
            Direction4AnimationSet(
                CharacterAnimation.Idle.Up(get<PlayerCharacterSpriteSheet>()),
                CharacterAnimation.Idle.Down(get<PlayerCharacterSpriteSheet>()),
                CharacterAnimation.Idle.Left(get<PlayerCharacterSpriteSheet>()),
                CharacterAnimation.Idle.Right(get<PlayerCharacterSpriteSheet>()),
            ),
        )

    class Walk :
        MovementAnimationSet.Walk(
            Direction4AnimationSet(
                CharacterAnimation.Walk.Up(get<PlayerCharacterSpriteSheet>()),
                CharacterAnimation.Walk.Down(get<PlayerCharacterSpriteSheet>()),
                CharacterAnimation.Walk.Left(get<PlayerCharacterSpriteSheet>()),
                CharacterAnimation.Walk.Right(get<PlayerCharacterSpriteSheet>()),
            ),
        )
}
