package pro.piechowski.highschoolstory.animation

import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World
import org.koin.core.component.KoinComponent
import pro.piechowski.highschoolstory.sprite.CurrentSprite

class AnimatedSpriteSystem :
    IteratingSystem(World.family { all(CurrentSprite, CurrentAnimation) }),
    KoinComponent {
    override fun onTickEntity(entity: Entity) {
        val currentAnimation = entity[CurrentAnimation]

        currentAnimation.time += deltaTime

        entity[CurrentSprite].sprite =
            currentAnimation.let {
                it.animation.getKeyFrame(it.time)
            }
    }
}
