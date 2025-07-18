package pro.piechowski.highschoolstory

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.Entity
import io.github.oshai.kotlinlogging.KLogger

fun KLogger.debug(
    component: Component<*>,
    entity: Entity,
) = debug { "$entity.$component" }
