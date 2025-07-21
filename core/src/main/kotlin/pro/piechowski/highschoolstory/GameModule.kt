package pro.piechowski.highschoolstory

import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.utils.viewport.FitViewport
import com.badlogic.gdx.utils.viewport.Viewport
import com.github.quillraven.fleks.World
import com.github.quillraven.fleks.configureWorld
import org.koin.dsl.module
import pro.piechowski.highschoolstory.animation.SpriteAnimationSystem
import pro.piechowski.highschoolstory.input.GameInputMultiplexer
import pro.piechowski.highschoolstory.interaction.InteractableDebugSystem
import pro.piechowski.highschoolstory.interaction.InteractionSystem
import pro.piechowski.highschoolstory.interaction.InteractorDebugSystem
import pro.piechowski.highschoolstory.interaction.input.InteractionInputProcessor
import pro.piechowski.highschoolstory.movement.animaiton.MovementAnimationSystem
import pro.piechowski.highschoolstory.movement.facedirection.FaceDirectionDebugSystem
import pro.piechowski.highschoolstory.movement.facedirection.FaceDirectionSystem
import pro.piechowski.highschoolstory.movement.input.ControllerMovementInputSystem
import pro.piechowski.highschoolstory.movement.input.MultiplexMovementInputSystem
import pro.piechowski.highschoolstory.movement.position.PositionChangeSystem
import pro.piechowski.highschoolstory.movement.velocity.VelocitySystem
import pro.piechowski.highschoolstory.sprite.CurrentSpritePositionSystem
import pro.piechowski.highschoolstory.sprite.SpriteRenderingSystem

val gameModule =
    module {
        single<Camera> { OrthographicCamera() }
        single<Viewport> { FitViewport(1280f, 720f, get()) }
        single { MovementAnimationSystem() }
        single { SpriteRenderingSystem() }
        single { CurrentSpritePositionSystem() }
        single { SpriteAnimationSystem() }
        single { ControllerMovementInputSystem() }
        single { MultiplexMovementInputSystem() }
        single { VelocitySystem() }
        single { PositionChangeSystem() }
        single { FaceDirectionSystem() }
        single { InteractionInputProcessor() }
        single { GameInputMultiplexer() }
        single { InteractionSystem() }
        single { InteractorDebugSystem() }
        single { FaceDirectionDebugSystem() }
        single { InteractableDebugSystem() }
        single { ShapeRenderer() }

        single<World> {
            configureWorld {
                systems {
                    add(get<ControllerMovementInputSystem>())
                    add(get<MultiplexMovementInputSystem>())
                    add(get<FaceDirectionSystem>())
                    // add(get<FaceDirectionDebugSystem>())
                    add(get<VelocitySystem>())
                    add(get<PositionChangeSystem>())

                    add(get<InteractionSystem>())
                    // add(get<InteractorDebugSystem>())
                    // add(get<InteractableDebugSystem>())

                    add(get<MovementAnimationSystem>())
                    add(get<SpriteAnimationSystem>())
                    add(get<CurrentSpritePositionSystem>())
                    add(get<SpriteRenderingSystem>())
                }
            }
        }
    }
