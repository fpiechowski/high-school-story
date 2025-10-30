plugins {
    kotlin("jvm")
    alias(libs.plugins.kotlin.serialization)
}

dependencies {
    implementation(libs.bundles.kotlin)
    implementation(libs.bundles.libgdx)
    implementation(libs.bundles.ktx)
    implementation(libs.bundles.logging)
    implementation(libs.bundles.hoplite)
    implementation(libs.koin)
    implementation(libs.fleks)
}
