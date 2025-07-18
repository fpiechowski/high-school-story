package pro.piechowski.highschoolstory

import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.utils.viewport.FitViewport
import com.badlogic.gdx.utils.viewport.Viewport
import com.github.quillraven.fleks.World
import com.github.quillraven.fleks.configureWorld
import org.koin.dsl.module
import pro.piechowski.highschoolstory.animation.AnimatedSpriteSystem
import pro.piechowski.highschoolstory.movement.ControllerMovementInputSystem
import pro.piechowski.highschoolstory.movement.FaceDirectionSystem
import pro.piechowski.highschoolstory.movement.MovementAnimationSystem
import pro.piechowski.highschoolstory.movement.MovementInputSystem
import pro.piechowski.highschoolstory.movement.PositionChangeSystem
import pro.piechowski.highschoolstory.movement.VelocitySystem
import pro.piechowski.highschoolstory.sprite.CurrentSpritePositionSystem
import pro.piechowski.highschoolstory.sprite.SpriteRenderingSystem

val gameModule =
    module {
        single<Camera> { OrthographicCamera() }
        single<Viewport> { FitViewport(1280f, 720f, get()) }
        single { MovementAnimationSystem() }
        single { SpriteRenderingSystem() }
        single { CurrentSpritePositionSystem() }
        single { AnimatedSpriteSystem() }
        single { ControllerMovementInputSystem() }
        single { MovementInputSystem() }
        single { VelocitySystem() }
        single { PositionChangeSystem() }
        single { FaceDirectionSystem() }
        single<World> {
            configureWorld {
                systems {
                    add(get<ControllerMovementInputSystem>())
                    add(get<MovementInputSystem>())
                    add(get<FaceDirectionSystem>())
                    add(get<VelocitySystem>())
                    add(get<PositionChangeSystem>())
                    add(get<MovementAnimationSystem>())
                    add(get<AnimatedSpriteSystem>())
                    add(get<CurrentSpritePositionSystem>())
                    add(get<SpriteRenderingSystem>())
                }
            }
        }
    }
