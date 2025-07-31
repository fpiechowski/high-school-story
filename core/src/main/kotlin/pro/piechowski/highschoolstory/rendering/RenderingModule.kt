package pro.piechowski.highschoolstory.rendering

import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import org.koin.dsl.module
import pro.piechowski.highschoolstory.animation.SpriteAnimationSystem
import pro.piechowski.highschoolstory.rendering.sprite.CurrentSpritePositionSystem
import pro.piechowski.highschoolstory.rendering.sprite.SpriteRenderingSystem

val RenderingModule =
    module {

        single { SpriteRenderingSystem() }
        single { CurrentSpritePositionSystem() }
        single { SpriteAnimationSystem() }
        single { ShapeRenderer() }
    }
