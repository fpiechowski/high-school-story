plugins {
    `kotlin-dsl` // enables plugin development
}

repositories {
    gradlePluginPortal()
    mavenCentral()
}

dependencies {
    implementation("org.graalvm.buildtools.native:org.graalvm.buildtools.native.gradle.plugin:0.9.28")
}
