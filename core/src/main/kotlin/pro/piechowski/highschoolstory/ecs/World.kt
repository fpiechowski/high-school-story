package pro.piechowski.highschoolstory.ecs

import com.github.quillraven.fleks.IntervalSystem
import com.github.quillraven.fleks.World
import com.github.quillraven.fleks.configureWorld
import org.koin.core.scope.Scope
import pro.piechowski.highschoolstory.Config
import pro.piechowski.highschoolstory.animation.SpriteAnimationSystem
import pro.piechowski.highschoolstory.camera.CameraMovementSystem
import pro.piechowski.highschoolstory.debug.highlight.DebugEntityHighlightRenderingSystem
import pro.piechowski.highschoolstory.debug.selection.DebugSelectionIndicatorRenderingSystem
import pro.piechowski.highschoolstory.debug.text.DebugTextSystem
import pro.piechowski.highschoolstory.interaction.InteractionSystem
import pro.piechowski.highschoolstory.interaction.interactable.InteractableDebugSystem
import pro.piechowski.highschoolstory.interaction.interactor.InteractorDebugSystem
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
                inputSystems.forEach { add(it) }
                gameSystems.forEach { add(it) }
                physicsSystems.forEach { add(it) }
                renderingSystems.forEach { add(it) }
            }
        }
    }

context(scope: Scope)
private val inputSystems
    get() =
        with(scope) {
            listOf(
                get<MovementControllerInputSystem>(),
                get<MovementMultiplexInputSystem>(),
            )
        }

context(scope: Scope)
private val gameSystems
    get() =
        with(scope) {
            listOf(
                get<FaceDirectionSystem>(),
                get<VelocitySystem>(),
                get<InteractionSystem>(),
            )
        }

context(scope: Scope)
private val physicsSystems
    get() =
        with(scope) {
            listOf(get<PhysicsWorldStepSystem>())
        }

context(scope: Scope)
private val renderingSystems
    get() =
        with(scope) {
            listOf(
                get<MovementAnimationSystem>(),
                get<SpriteAnimationSystem>(),
                get<CurrentSpritePositionSystem>(),
                get<MapRenderingSystem.Background>(),
                get<SpriteRenderingSystem>(),
                get<MapRenderingSystem.Foreground>(),
                get<CameraMovementSystem>(),
            ).let {
                if (get<Config>().debug) {
                    it + debugSystems
                } else {
                    it
                }
            }
        }

context(scope: Scope)
private val debugSystems: List<IntervalSystem>
    get() =
        with(scope) {
            listOf(
                get<FaceDirectionDebugSystem>(),
                get<InteractorDebugSystem>(),
                get<InteractableDebugSystem>(),
                get<DebugTextSystem>(),
                get<PhysicsDebugRenderingSystem>(),
                get<DebugSelectionIndicatorRenderingSystem>(),
                // get<DebugEntityHighlightRenderingSystem>(),
            )
        }
