package pro.piechowski.highschoolstory.ecs.rendering

import com.github.quillraven.fleks.IntervalSystem
import com.github.quillraven.fleks.SystemConfiguration
import org.koin.core.scope.Scope
import pro.piechowski.highschoolstory.ecs.systemWrapper
import pro.piechowski.highschoolstory.light.LightRenderingSystem
import pro.piechowski.highschoolstory.map.MapRenderingSystem
import pro.piechowski.highschoolstory.sprite.SpriteRenderingSystem
import pro.piechowski.highschoolstory.transition.FadeTransitionSystem

context(scope: Scope, systemConfiguration: SystemConfiguration)
fun setupRenderingSystems() =
    with(scope) {
        with(systemConfiguration) {
            spriteRenderingSystems.forEach { add(it) }

            add(get<LightRenderingSystem>())

            add(get<FadeTransitionSystem>())
        }
    }

context(scope: Scope)
private val spriteRenderingSystems: List<IntervalSystem>
    get() =
        with(scope) {
            listOf(
                get<MapRenderingSystem.Background>(),
                get<SpriteRenderingSystem>(),
                get<MapRenderingSystem.Foreground>(),
            )
        }
