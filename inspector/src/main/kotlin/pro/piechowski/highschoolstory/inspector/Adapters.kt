package pro.piechowski.highschoolstory.inspector

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.core.annotation.KoinInternalApi
import pro.piechowski.highschoolstory.inspector.ecs.FleksECS
import pro.piechowski.highschoolstory.inspector.ecs.getFleksWorldFrom
import pro.piechowski.highschoolstory.inspector.koin.KoinGlobalInstances
import pro.piechowski.highschoolstory.inspector.runtime.LibGDXRuntime

@DelicateCoroutinesApi
@ExperimentalCoroutinesApi
@KoinInternalApi
class Adapters {
    private val koinGlobalInstances = KoinGlobalInstances()

    val globalInstances = koinGlobalInstances
    val runtime = LibGDXRuntime()
    val ecs = FleksECS(getFleksWorldFrom(koinGlobalInstances))
}
