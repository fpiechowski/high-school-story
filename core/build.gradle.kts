plugins {
    kotlin("plugin.serialization")
    id("com.google.devtools.ksp")
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
    implementation("pro.piechowski.kge:core")
    implementation("pro.piechowski.kge:character")
    implementation("pro.piechowski.kge:story")
    implementation("pro.piechowski.kge:dialogue")
    implementation("pro.piechowski.kge:interaction")
    implementation("pro.piechowski.kge:time")
    implementation("pro.piechowski.kge:scene")
    implementation("pro.piechowski.kge:weather")
    implementation("pro.piechowski.kge:vehicle")
    implementation("pro.piechowski.kge:power")

    api(libs.bundles.kotlin)
    api(libs.bundles.libgdx)
    api(libs.bundles.ktx)
    api(libs.bundles.logging)
    api(libs.bundles.hoplite)
    api(libs.koin)
    api(libs.fleks)
    api(libs.arrow.fx.coroutines)
}

tasks.test {
    useJUnitPlatform()
}
