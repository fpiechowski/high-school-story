package pro.piechowski.highschoolstory.inspector.ecs

import com.github.quillraven.fleks.World
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.KoinInternalApi
import pro.piechowski.highschoolstory.ecs.WorldManager
import pro.piechowski.highschoolstory.inspector.koin.KoinGlobalInstances
import pro.piechowski.highschoolstory.koinModulesInitialized

@ExperimentalCoroutinesApi
@KoinInternalApi
fun getFleksWorldFrom(koinGlobalInstances: KoinGlobalInstances) =
    koinGlobalInstances.koin
        .combine(koinModulesInitialized) { koin, modulesInitialized ->
            if (modulesInitialized) koin else null
        }.filterNotNull()
        .flatMapLatest { koin ->
            koin
                .get<WorldManager>()
                .worldInitialized
                .map { worldInitialized ->
                    if (worldInitialized) koin.get<World>() else null
                }
        }
