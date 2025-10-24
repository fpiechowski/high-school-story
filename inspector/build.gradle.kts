plugins {
    java
    application
    id("org.jetbrains.kotlin.jvm") version "2.2.20"
    id("org.openjfx.javafxplugin") version "0.0.13"
    id("org.beryx.jlink") version "2.25.0"
    id("io.github.fpiechowski.hex") version "1.0.3"
}

group = "pro.piechowski.highschoolstory"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val junitVersion = "5.12.1"

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

application {
    mainClass.set("pro.piechowski.highschoolstory.inspector.InspectorLauncherKt")
}

kotlin {
    jvmToolchain(21)
}

javafx {
    version = "21.0.6"
    modules = listOf("javafx.controls", "javafx.fxml")
}

dependencies {
    domainImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")

    implementation(project(":core"))
    implementation(project(":lwjgl3"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-javafx:1.10.2")
    implementation("org.jetbrains.kotlinx:atomicfu:0.29.0")
    implementation("io.github.classgraph:classgraph:4.8.165")

    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

jlink {
    imageZip.set(layout.buildDirectory.file("/distributions/app-${javafx.platform.classifier}.zip"))
    options.set(listOf("--strip-debug", "--compress", "2", "--no-header-files", "--no-man-pages"))
    launcher {
        name = "app"
    }
}

val openAllModulesArgs =
    listOf(
        "--add-opens",
        "java.base/java.lang=ALL-UNNAMED",
        "--add-opens",
        "java.base/java.lang.invoke=ALL-UNNAMED",
        "--add-opens",
        "java.base/java.lang.reflect=ALL-UNNAMED",
        "--add-opens",
        "java.base/java.util=ALL-UNNAMED",
        "--add-opens",
        "java.base/java.time=ALL-UNNAMED",
        "--add-opens",
        "java.base/java.io=ALL-UNNAMED",
        "--add-opens",
        "java.base/java.net=ALL-UNNAMED",
        "--add-opens",
        "java.base/java.math=ALL-UNNAMED",
        "--add-opens",
        "java.base/java.text=ALL-UNNAMED",
        "--add-opens",
        "java.desktop/java.awt=ALL-UNNAMED",
        "--add-opens",
        "java.desktop/sun.awt=ALL-UNNAMED",
        "--add-opens",
        "java.sql/java.sql=ALL-UNNAMED",
        "--add-opens",
        "javafx.base/com.sun.javafx.property.adapter=ALL-UNNAMED",
        "--add-opens",
        "javafx.graphics/com.sun.javafx.scene=ALL-UNNAMED",
        "--add-opens",
        "javafx.controls/com.sun.javafx.scene.control=ALL-UNNAMED",
    )

tasks.withType<JavaExec>().configureEach {
    jvmArgs(openAllModulesArgs)
}

tasks.matching { it.name == "jlink" || it.name == "run" }.configureEach {
    if (this is JavaExec) jvmArgs(openAllModulesArgs)
}
