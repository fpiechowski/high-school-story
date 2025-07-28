plugins {
    kotlin("plugin.serialization")
    id("com.google.devtools.ksp")
}

tasks {
    compileJava {
        options.encoding = "UTF-8"
    }
    compileTestJava {
        options.encoding = "UTF-8"
    }
}

val appName: String by project

eclipse.project.name = "$appName-core"

val ashleyVersion: String by project
val box2dlightsVersion: String by project
val gdxControllersVersion: String by project
val aiVersion: String by project
val gdxVersion: String by project
val visUiVersion: String by project
val ktxVersion: String by project
val fleksVersion: String by project
val artemisOdbVersion: String by project
val kotlinVersion: String by project
val kotlinxCoroutinesVersion: String by project
val graalHelperVersion: String by project
val enableGraalNative: String by project
val koinVersion: String by project
val koinAnnotationsVersion: String by project

dependencies {
    api("com.badlogicgames.ashley:ashley:$ashleyVersion")
    api("com.badlogicgames.box2dlights:box2dlights:$box2dlightsVersion")
    api("com.badlogicgames.gdx-controllers:gdx-controllers-core:$gdxControllersVersion")
    api("com.badlogicgames.gdx:gdx-ai:$aiVersion")
    api("com.badlogicgames.gdx:gdx-box2d:$gdxVersion")
    api("com.badlogicgames.gdx:gdx-freetype:$gdxVersion")
    api("com.badlogicgames.gdx:gdx:$gdxVersion")
    api("com.kotcrab.vis:vis-ui:$visUiVersion")
    api("io.github.libktx:ktx-actors:$ktxVersion")
    api("io.github.libktx:ktx-ai:$ktxVersion")
    api("io.github.libktx:ktx-app:$ktxVersion")
    api("io.github.libktx:ktx-assets-async:$ktxVersion")
    api("io.github.libktx:ktx-assets:$ktxVersion")
    api("io.github.libktx:ktx-async:$ktxVersion")
    api("io.github.libktx:ktx-box2d:$ktxVersion")
    api("io.github.libktx:ktx-collections:$ktxVersion")
    api("io.github.libktx:ktx-freetype-async:$ktxVersion")
    api("io.github.libktx:ktx-freetype:$ktxVersion")
    api("io.github.libktx:ktx-graphics:$ktxVersion")
    api("io.github.libktx:ktx-i18n:$ktxVersion")
    api("io.github.libktx:ktx-inject:$ktxVersion")
    api("io.github.libktx:ktx-json:$ktxVersion")
    api("io.github.libktx:ktx-log:$ktxVersion")
    api("io.github.libktx:ktx-math:$ktxVersion")
    api("io.github.libktx:ktx-preferences:$ktxVersion")
    api("io.github.libktx:ktx-reflect:$ktxVersion")
    api("io.github.libktx:ktx-scene2d:$ktxVersion")
    api("io.github.libktx:ktx-style:$ktxVersion")
    api("io.github.libktx:ktx-tiled:$ktxVersion")
    api("io.github.libktx:ktx-vis-style:$ktxVersion")
    api("io.github.libktx:ktx-vis:$ktxVersion")
    api("io.github.quillraven.fleks:Fleks:$fleksVersion")
    api("net.onedaybeard.artemis:artemis-odb:$artemisOdbVersion")
    api("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinxCoroutinesVersion")

    api("io.insert-koin:koin-core:$koinVersion")
    api("org.jetbrains.kotlinx:kotlinx-serialization-json:1.9.0")
    api("io.github.oshai:kotlin-logging-jvm:7.0.7")
    api("org.slf4j:slf4j-api:2.0.17")
    api("ch.qos.logback:logback-classic:1.5.18")
    api("com.sksamuel.hoplite:hoplite-core:2.9.0")
    api("com.sksamuel.hoplite:hoplite-yaml:2.9.0")

    if (enableGraalNative == "true") {
        implementation("io.github.berstanio:gdx-svmhelper-annotations:$graalHelperVersion")
    }

    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.13.4")
    testImplementation("io.kotest:kotest-assertions-core:5.9.1")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.add("-Xcontext-parameters")
    }
}

tasks.test {
    useJUnitPlatform()
}
