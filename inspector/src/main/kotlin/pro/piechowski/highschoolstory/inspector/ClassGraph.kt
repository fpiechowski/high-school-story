package pro.piechowski.highschoolstory.inspector

import io.github.classgraph.ClassGraph

val classGraph: ClassGraph
    get() =
        ClassGraph()
            .enableAllInfo()
            .enableClassInfo()
            .enableExternalClasses()
            .ignoreClassVisibility()
            .enableSystemJarsAndModules()
