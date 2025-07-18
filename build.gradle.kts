plugins {
    idea
    eclipse
    kotlin("jvm") version "2.2.0"
    kotlin("plugin.serialization") version "2.2.0"
    id("org.jlleitschuh.gradle.ktlint") version "13.0.0"
}

repositories {
    mavenCentral()
    maven { url = uri("https://s01.oss.sonatype.org") }
    gradlePluginPortal()
    mavenLocal()
    google()
    maven { url = uri("https://oss.sonatype.org/content/repositories/snapshots/") }
    maven { url = uri("https://s01.oss.sonatype.org/content/repositories/snapshots/") }
}

buildscript {
    repositories {
        mavenCentral()
        maven { url = uri("https://s01.oss.sonatype.org") }
        gradlePluginPortal()
        mavenLocal()
        google()
        maven { url = uri("https://oss.sonatype.org/content/repositories/snapshots/") }
        maven { url = uri("https://s01.oss.sonatype.org/content/repositories/snapshots/") }
    }
    dependencies {
        val kotlinVersion = "2.2.0"

        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
    }
}

allprojects {
    apply(plugin = "eclipse")
    apply(plugin = "idea")

    // This allows you to "Build and run using IntelliJ IDEA", an option in IDEA"s Settings.
    idea {
        module {
            outputDir = file("build/classes/java/main")
            testOutputDir = file("build/classes/java/test")
        }
    }
}

configure(subprojects) {
    apply(plugin = "java-library")
    apply(plugin = "kotlin")
    apply(plugin = "org.jlleitschuh.gradle.ktlint")

    java {
        sourceCompatibility = JavaVersion.VERSION_21
    }

    // From https://lyze.dev/2021/04/29/libGDX-Internal-Assets-List/
    // The article can be helpful when using assets.txt in your project.
    tasks.register("generateAssetList") {
        inputs.dir("${project.rootDir}/assets/")
        // projectFolder/assets
        val assetsFolder = File("${project.rootDir}/assets/")
        // projectFolder/assets/assets.txt
        val assetsFile = File(assetsFolder, "assets.txt")
        // delete that file in case we'"'ve already created it
        assetsFile.delete()

        // iterate through all files inside that folder
        // convert it to a relative path
        // and append it to the file assets.txt
        fileTree(assetsFolder).map { it.relativeTo(assetsFolder) }.sorted().forEach {
            assetsFile.appendText(it.path + "\n")
        }
    }

    tasks {
        processResources {
            dependsOn("generateAssetList")
        }

        compileJava {
            options.setIncremental(true)
        }

        compileKotlin {
            compilerOptions.jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21)
        }

        compileTestKotlin {
            compilerOptions.jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21)
        }
    }
}

subprojects {
    val projectVersion: String by project

    version = projectVersion
    ext["appName"] = "HighSchoolStory"
    repositories {
        mavenCentral()
        maven { url = uri("https://s01.oss.sonatype.org") }
        // You may want to remove the following line if you have errors downloading dependencies.
        mavenLocal()
        maven { url = uri("https://oss.sonatype.org/content/repositories/snapshots/") }
        maven { url = uri("https://s01.oss.sonatype.org/content/repositories/snapshots/") }
        maven { url = uri("https://jitpack.io") }
    }
}

eclipse.project.name = "HighSchoolStory" + "-parent"
