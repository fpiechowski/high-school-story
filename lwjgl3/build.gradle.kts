import io.github.fourlastor.construo.Target
import java.util.*

val enableGraalNative: String by project

plugins {
    application
    kotlin("jvm")
    id("io.github.fourlastor.construo") version "1.7.1"
    id("native-image-plugin")
}

sourceSets.main { resources.srcDirs(rootProject.file("assets").path) }

val mainClassName = "pro.piechowski.highschoolstory.lwjgl3.Lwjgl3Launcher"
application.mainClass = mainClassName
val appName: String by project
eclipse.project.name = "$appName-lwjgl3"
java.sourceCompatibility = JavaVersion.VERSION_21
java.targetCompatibility = JavaVersion.VERSION_21
if (JavaVersion.current().isJava9Compatible) {
    tasks.compileJava { options.release.set(21) }
}
kotlin.compilerOptions.jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21)

dependencies {
    val gdxControllersVersion: String by project
    val gdxVersion: String by project
    implementation("com.badlogicgames.gdx-controllers:gdx-controllers-desktop:$gdxControllersVersion")
    implementation("com.badlogicgames.gdx:gdx-backend-lwjgl3:$gdxVersion")
    implementation("com.badlogicgames.gdx:gdx-box2d-platform:$gdxVersion:natives-desktop")
    implementation("com.badlogicgames.gdx:gdx-freetype-platform:$gdxVersion:natives-desktop")
    implementation("com.badlogicgames.gdx:gdx-platform:$gdxVersion:natives-desktop")
    implementation(project(":core"))
    implementation("com.badlogicgames.gdx:gdx-tools:$gdxVersion") {
        exclude(group = "com.badlogicgames.gdx", module = "gdx-backend-lwjgl")
    }

    val enableGraalNative: String by project
    val graalHelperVersion: String by project
    if (enableGraalNative == "true") {
        implementation("io.github.berstanio:gdx-svmhelper-backend-lwjgl3:$graalHelperVersion")
        implementation("io.github.berstanio:gdx-svmhelper-extension-box2d:$graalHelperVersion")
        implementation("io.github.berstanio:gdx-svmhelper-extension-freetype:$graalHelperVersion")

    }
}

val os = System.getProperty("os.name").lowercase(Locale.getDefault())

tasks.named<JavaExec>("run") {
    workingDir = rootProject.file("assets")
    // You can uncomment the next line if your IDE claims a build failure even when the app closed properly.
    //setIgnoreExitValue(true)

    if (os.contains("mac")) jvmArgs("-XstartOnFirstThread")
}

val projectVersion: String by project

tasks.jar {
    // sets the name of the .jar file this produces to the name of the game or app, with the version after.
    archiveFileName.set("${appName}-${projectVersion}.jar")
    // the duplicatesStrategy matters starting in Gradle 7.0; this setting works.
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    dependsOn(configurations.getByName("runtimeClasspath"))
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory()) it else zipTree(it) })
    // these "exclude" lines remove some unnecessary duplicate files in the output JAR.
    exclude("META-INF/INDEX.LIST", "META-INF/*.SF", "META-INF/*.DSA", "META-INF/*.RSA")
    dependencies {
        exclude("META-INF/INDEX.LIST", "META-INF/maven/**")
    }
    // setting the manifest makes the JAR runnable.
    manifest {
        attributes(mapOf("Main-Class" to mainClassName))
    }
    // this last step may help on some OSes that need extra instruction to make runnable JARs.
    doLast {
        file(archiveFile).setExecutable(true, false)
    }
}

// Builds a JAR that only includes the files needed to run on macOS, not Windows or Linux.
// The file size for a Mac-only JAR is about 7MB smaller than a cross-platform JAR.
tasks.register<Jar>("jarMac") {
    dependsOn(tasks.named("jar"))
    group = "build"


    archiveFileName.set("${appName}-${projectVersion}-mac.jar")
    exclude(
        "windows/x86/**", "windows/x64/**", "linux/arm32/**", "linux/arm64/**", "linux/x64/**", "**/*.dll", "**/*.so",
        "META-INF/INDEX.LIST", "META-INF/*.SF", "META-INF/*.DSA", "META-INF/*.RSA"
    )
    dependencies {
        exclude(
            "windows/x86/**", "windows/x64/**", "linux/arm32/**", "linux/arm64/**", "linux/x64/**",
            "META-INF/INDEX.LIST", "META-INF/maven/**"
        )
    }
}

// Builds a JAR that only includes the files needed to run on Linux, not Windows or macOS.
// The file size for a Linux-only JAR is about 5MB smaller than a cross-platform JAR.
tasks.register<Jar>("jarLinux") {
    dependsOn("jar")
    group = "build"
    archiveFileName.set("${appName}-${projectVersion}-linux.jar")
    exclude(
        "windows/x86/**", "windows/x64/**", "macos/arm64/**", "macos/x64/**", "**/*.dll", "**/*.dylib",
        "META-INF/INDEX.LIST", "META-INF/*.SF", "META-INF/*.DSA", "META-INF/*.RSA"
    )
    dependencies {
        exclude(
            "windows/x86/**", "windows/x64/**", "macos/arm64/**", "macos/x64/**",
            "META-INF/INDEX.LIST", "META-INF/maven/**"
        )
    }
}

// Builds a JAR that only includes the files needed to run on Windows, not Linux or macOS.
// The file size for a Windows-only JAR is about 6MB smaller than a cross-platform JAR.
tasks.register<Jar>("jarWin") {
    dependsOn("jar")
    group = "build"
    archiveFileName.set("${appName}-${projectVersion}-win.jar")
    exclude(
        "macos/arm64/**", "macos/x64/**", "linux/arm32/**", "linux/arm64/**", "linux/x64/**", "**/*.dylib", "**/*.so",
        "META-INF/INDEX.LIST", "META-INF/*.SF", "META-INF/*.DSA", "META-INF/*.RSA"
    )
    dependencies {
        exclude(
            "macos/arm64/**", "macos/x64/**", "linux/arm32/**", "linux/arm64/**", "linux/x64/**",
            "META-INF/INDEX.LIST", "META-INF/maven/**"
        )
    }
}

construo {
    // name of the executable
    name.set(appName)
    // human-readable name, used for example in the `.app` name for macOS
    humanName.set(appName)
    // Optional, defaults to project version property
    version.set(projectVersion)

    targets {
        register<Target.Linux>("linuxX64") {
            architecture.set(Target.Architecture.X86_64)
            jdkUrl.set("https://github.com/adoptium/temurin17-binaries/releases/download/jdk-17.0.15%2B6/OpenJDK17U-jdk_x64_linux_hotspot_17.0.15_6.tar.gz")
            // Linux does not currently have a way to set the icon on the executable
        }
        register<Target.MacOs>("macM1") {
            architecture.set(Target.Architecture.AARCH64)
            jdkUrl.set("https://github.com/adoptium/temurin17-binaries/releases/download/jdk-17.0.15%2B6/OpenJDK17U-jdk_aarch64_mac_hotspot_17.0.15_6.tar.gz")
            // macOS needs an identifier
            identifier.set("pro.piechowski.highschoolstory." + appName)
            // Optional: icon for macOS, as an ICNS file
            macIcon.set(project.file("icons/logo.icns"))
        }
        register<Target.MacOs>("macX64") {
            architecture.set(Target.Architecture.X86_64)
            jdkUrl.set("https://github.com/adoptium/temurin17-binaries/releases/download/jdk-17.0.15%2B6/OpenJDK17U-jdk_x64_mac_hotspot_17.0.15_6.tar.gz")
            // macOS needs an identifier
            identifier.set("pro.piechowski.highschoolstory." + appName)
            // Optional: icon for macOS, as an ICNS file
            macIcon.set(project.file("icons/logo.icns"))
        }
        register<Target.Windows>("winX64") {
            architecture.set(Target.Architecture.X86_64)
            // Optional: icon for Windows, as a PNG
            icon.set(project.file("icons/logo.png"))
            jdkUrl.set("https://github.com/adoptium/temurin17-binaries/releases/download/jdk-17.0.15%2B6/OpenJDK17U-jdk_x64_windows_hotspot_17.0.15_6.zip")
            // Uncomment the next line to show a console when the game runs, to print messages.
            //useConsole.set(true)
        }
    }
}


// Equivalent to the jar task; here for compatibility with gdx-setup.
tasks.register("dist") {
    dependsOn("jar")
}

distributions {
    main {
        contents {
            into("libs") {
                project.configurations.runtimeClasspath.get().files.filter { file ->
                    file.getName() != project.tasks.jar.get().outputs.files.singleFile.name
                }.forEach { file ->
                    exclude(file.name)
                }
            }
        }
    }
}

tasks.named<CreateStartScripts>("startScripts") {
    dependsOn(":lwjgl3:jar")
    classpath = files(project.tasks.named("jar").get().outputs.files)
}
