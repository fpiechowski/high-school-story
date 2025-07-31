package pro.piechowski.highschoolstory.ecs

import com.github.quillraven.fleks.SystemConfiguration
import com.github.quillraven.fleks.World
import com.github.quillraven.fleks.configureWorld
import org.koin.core.scope.Scope
import pro.piechowski.highschoolstory.Config
import pro.piechowski.highschoolstory.animation.SpriteAnimationSystem
import pro.piechowski.highschoolstory.camera.CameraMovementSystem
import pro.piechowski.highschoolstory.debug.DebugTextSystem
import pro.piechowski.highschoolstory.interaction.InteractableDebugSystem
import pro.piechowski.highschoolstory.interaction.InteractionSystem
import pro.piechowski.highschoolstory.interaction.InteractorDebugSystem
import pro.piechowski.highschoolstory.map.MapRenderingSystem
import pro.piechowski.highschoolstory.physics.body.PhysicsDebugRenderingSystem
import pro.piechowski.highschoolstory.physics.body.PhysicsWorldStepSystem
import pro.piechowski.highschoolstory.physics.movement.animation.MovementAnimationSystem
import pro.piechowski.highschoolstory.physics.movement.facedirection.FaceDirectionDebugSystem
import pro.piechowski.highschoolstory.physics.movement.facedirection.FaceDirectionSystem
import pro.piechowski.highschoolstory.physics.movement.input.MovementControllerInputSystem
import pro.piechowski.highschoolstory.physics.movement.input.MovementMultiplexInputSystem
import pro.piechowski.highschoolstory.physics.movement.velocity.VelocitySystem
import pro.piechowski.highschoolstory.rendering.sprite.CurrentSpritePositionSystem
import pro.piechowski.highschoolstory.rendering.sprite.SpriteRenderingSystem

context(scope: Scope)
operator fun World.Companion.invoke() =
    with(scope) {
        configureWorld {
            systems {
                inputSystems()
                gameSystems()
                physicsSystems()
                renderingSystems {
                    if (get<Config>().debug) {
                        debugSystems()
                    }
                }
            }
        }
    }

context(sc: SystemConfiguration, scope: Scope)
private fun inputSystems() =
    with(sc) {
        with(scope) {
            add(get<MovementControllerInputSystem>())
            add(get<MovementMultiplexInputSystem>())
        }
    }

context(sc: SystemConfiguration, scope: Scope)
private fun gameSystems() =
    with(sc) {
        with(scope) {
            add(get<FaceDirectionSystem>())
            add(get<VelocitySystem>())
            add(get<InteractionSystem>())
        }
    }

context(sc: SystemConfiguration, scope: Scope)
private fun physicsSystems() =
    with(sc) {
        with(scope) {
            add(get<PhysicsWorldStepSystem>())
        }
    }

context(sc: SystemConfiguration, scope: Scope)
private fun renderingSystems(inBatch: () -> Unit = {}) =
    with(sc) {
        with(scope) {
            // add(get<BeginRenderingBatchSystem>())
            add(get<CameraMovementSystem>())
            add(get<MovementAnimationSystem>())
            add(get<SpriteAnimationSystem>())
            add(get<CurrentSpritePositionSystem>())
            add(get<MapRenderingSystem.Background>())
            add(get<SpriteRenderingSystem>())
            add(get<MapRenderingSystem.Foreground>())
            inBatch()
            // add(get<EndRenderingBatchSystem>())
        }
    }

context(sc: SystemConfiguration, scope: Scope)
private fun debugSystems() =
    with(sc) {
        with(scope) {
            add(get<FaceDirectionDebugSystem>())
            add(get<InteractorDebugSystem>())
            add(get<InteractableDebugSystem>())
            add(get<DebugTextSystem>())
            add(get<PhysicsDebugRenderingSystem>())
        }
    }
