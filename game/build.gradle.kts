plugins {
    idea
    eclipse
    kotlin("jvm")
    kotlin("plugin.serialization")
    id("org.jlleitschuh.gradle.ktlint")
    id("com.google.devtools.ksp")
}

subprojects {
    apply(plugin = "java-library")
    apply(plugin = "kotlin")
    apply(plugin = "org.jlleitschuh.gradle.ktlint")

    java {
        sourceCompatibility = JavaVersion.VERSION_17
    }

    val generateAssetList by tasks.registering {
        inputs.dir("${project.projectDir}/assets/")
        val assetsFolder = File("${project.projectDir}/assets/")
        val assetsFile = File(assetsFolder, "assets.txt")
        assetsFile.delete()

        fileTree(assetsFolder).map { it.relativeTo(assetsFolder) }.sorted().forEach {
            assetsFile.appendText(it.path + "\n")
        }
    }

    tasks {
        processResources {
            dependsOn(generateAssetList)
        }
    }

    val projectVersion: String by project

    version = projectVersion
    ext["appName"] = "HighSchoolStory"
}

eclipse.project.name = "HighSchoolStory" + "-parent"
