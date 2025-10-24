package pro.piechowski.highschoolstory.inspector.ecs

import com.github.quillraven.fleks.ComponentService
import com.github.quillraven.fleks.ComponentsHolder
import com.github.quillraven.fleks.World
import com.github.quillraven.fleks.collection.Bag
import io.github.classgraph.ClassGraph
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import pro.piechowski.highschoolstory.inspector.fullTypeName
import pro.piechowski.highschoolstory.inspector.propertyValue
import com.github.quillraven.fleks.Component as FleksComponent
import com.github.quillraven.fleks.ComponentType as FleksComponentType

@ExperimentalCoroutinesApi
class FleksECS(
    private val world: Flow<World?>,
) : ECS {
    private val logger = KotlinLogging.logger { }

    private val World.componentService
        get() = propertyValue<World, ComponentService>("componentService")

    private val ComponentService.holdersBag: Bag<ComponentsHolder<FleksComponent<Any>>?>
        get() = propertyValue("holdersBag")

    private val <T> Bag<T>.values
        get() = propertyValue<Bag<T>, Array<T>>("values")

    override val componentTypes: List<ECS.ComponentType>
        get() =
            ClassGraph()
                .enableAllInfo()
                .enableClassInfo()
                .enableExternalClasses()
                .ignoreClassVisibility()
                .enableSystemJarsAndModules()
                .scan()
                .use { scan ->
                    scan.allClasses
                        .filter { it.superclasses.any { superclass -> superclass.name == FleksComponentType::class.qualifiedName } }
                        .filter { it.packageName != "com.github.quillraven.fleks" }
                        .mapNotNull { classInfo ->
                            classInfo.loadClass() as Class<FleksComponentType<Any>>
                        }
                }.map { ECS.ComponentType(it.name) }
                .also {
                    logger.info { "Found ${it.size} component types" }
                }

    override val entityComponents: Flow<Map<ECS.Entity, List<ECS.Component>>> =
        world.flatMapLatest { world ->
            flow {
                while (true) {
                    val entityComponents =
                        world
                            ?.let { world ->
                                world.componentService
                                    .holdersBag
                                    .values
                                    .filterNotNull()
                                    .flatMap { holder ->
                                        world.asEntityBag().map { entity ->
                                            entity to holder.getOrNull(entity)
                                        }
                                    }.filter { it.second != null }
                                    .map { it.first to it.second!! }
                                    .map {
                                        ECS.Entity(it.first.id) to
                                            ECS.Component(
                                                ECS.ComponentType(it.second::class.fullTypeName),
                                                it.second,
                                            )
                                    }.groupBy(keySelector = { it.first }) {
                                        it.second
                                    }
                            } ?: emptyMap()

                    emit(entityComponents)

                    delay(2000)
                }
            }
        }
}
