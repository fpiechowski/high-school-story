package pro.piechowski.highschoolstory.rendering

import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import org.koin.dsl.module
import pro.piechowski.highschoolstory.animation.SpriteAnimationSystem
import pro.piechowski.highschoolstory.shader.ShaderManager
import pro.piechowski.highschoolstory.sprite.CurrentSpritePositionSystem
import pro.piechowski.highschoolstory.sprite.SpriteRenderingSystem

val RenderingModule =
    module {
        single { SpriteRenderingSystem() }
        single { CurrentSpritePositionSystem() }
        single { SpriteAnimationSystem() }
        single { ShapeRenderer() }
        single { ShaderManager() }
    }
