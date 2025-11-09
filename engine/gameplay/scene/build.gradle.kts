plugins {
    kotlin("jvm")
    alias(libs.plugins.kotlin.serialization)
}

dependencies {
    implementation(project(":engine:core"))

    implementation(libs.bundles.kotlin)
    implementation(libs.bundles.libgdx)
    implementation(libs.libgdx.lwjgl3)
    implementation(libs.bundles.ktx)
    implementation(libs.bundles.logging)
    implementation(libs.bundles.hoplite)
    implementation(libs.bundles.ktor.server)
    implementation(libs.koin)
    implementation(libs.fleks)
    implementation(libs.classgraph)
    implementation(libs.junit)
    implementation(libs.kotest.assertions)
}
