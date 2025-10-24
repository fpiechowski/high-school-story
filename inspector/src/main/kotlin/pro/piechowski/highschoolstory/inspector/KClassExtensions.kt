package pro.piechowski.highschoolstory.inspector

import org.koin.ext.getFullName
import kotlin.reflect.KClass

val KClass<*>.fullTypeName get() =
    this.qualifiedName?.removePrefix(this.java.`package`.name + ".") ?: "Unknown"
