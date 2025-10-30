import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    idea
    eclipse
    kotlin("jvm") version "2.2.20"
    kotlin("plugin.serialization") version "2.2.20"
    id("org.jlleitschuh.gradle.ktlint") version "13.0.0"
    id("com.google.devtools.ksp") version "2.2.20-2.0.4"
}

allprojects {
    apply(plugin = "eclipse")
    apply(plugin = "idea")

    idea {
        module {
            outputDir = file("build/classes/java/main")
            testOutputDir = file("build/classes/java/test")
        }
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

    plugins.withId("org.jetbrains.kotlin.jvm") {
        tasks {
            compileKotlin {
                compilerOptions.jvmTarget.set(JvmTarget.JVM_17)
            }

            compileTestKotlin {
                compilerOptions.jvmTarget.set(JvmTarget.JVM_17)
            }
        }

        kotlin {
            jvmToolchain(17)
            compilerOptions {
                freeCompilerArgs.add("-Xcontext-parameters")
                jvmTarget.set(JvmTarget.JVM_17)
            }
        }
    }

    plugins.withId("java") {
        tasks.compileJava {
            options.setIncremental(true)
            options.encoding = "UTF-8"
        }

        tasks.compileTestJava {
            options.encoding = "UTF-8"
        }

        java {
            toolchain.languageVersion.set(JavaLanguageVersion.of(17))
            sourceCompatibility = JavaVersion.VERSION_17
            targetCompatibility = JavaVersion.VERSION_17
        }
    }
}
