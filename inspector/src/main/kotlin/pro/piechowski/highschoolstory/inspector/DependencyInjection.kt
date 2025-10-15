package pro.piechowski.highschoolstory.inspector

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.newSingleThreadContext
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.instance
import org.koin.core.annotation.KoinInternalApi
import pro.piechowski.highschoolstory.inspector.ecs.EcsInspector
import pro.piechowski.highschoolstory.inspector.game.GameInspector
import pro.piechowski.highschoolstory.inspector.koin.KoinInspector

@KoinInternalApi
fun DI.MainBuilder.dependencyBuilder() {
    bindSingleton(gameScopeTag) { CoroutineScope(SupervisorJob() + newSingleThreadContext("Game")) }
    bindSingleton { MutableStateFlow<Job?>(null) }

    bindSingleton { GameInspector(instance(gameScopeTag)) }
    bindSingleton { EcsInspector(instance(gameScopeTag)) }
    bindSingleton { KoinInspector(instance(gameScopeTag)) }
}
