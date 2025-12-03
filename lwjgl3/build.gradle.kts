import io.github.fourlastor.construo.Target
import java.util.Locale

val enableGraalNative: String by project

plugins {
    application
    kotlin("jvm")
    alias(libs.plugins.construo)
}

sourceSets.main { resources.srcDirs(parent!!.file("assets").path) }

val mainClassName = "pro.piechowski.highschoolstory.lwjgl3.Lwjgl3Launcher"
application.mainClass = mainClassName

val appName: String by project
eclipse.project.name = "$appName-lwjgl3"

if (JavaVersion.current().isJava9Compatible) {
    tasks.compileJava { options.release.set(17) }
}

dependencies {
    val gdxControllersVersion: String by project
    val gdxVersion: String by project
    implementation("com.badlogicgames.gdx-controllers:gdx-controllers-desktop:$gdxControllersVersion")
    implementation("com.badlogicgames.gdx:gdx-backend-lwjgl3:$gdxVersion")
    implementation("com.badlogicgames.gdx:gdx-box2d-platform:$gdxVersion:natives-desktop")
    implementation("com.badlogicgames.gdx:gdx-freetype-platform:$gdxVersion:natives-desktop")
    implementation("com.badlogicgames.gdx:gdx-platform:$gdxVersion:natives-desktop")

    implementation(project(":core"))

    implementation("pro.piechowski.kge:core")
    implementation("pro.piechowski.kge:character")
    implementation("pro.piechowski.kge:scene")

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
    workingDir = parent!!.file("assets")

    if (os.contains("mac")) jvmArgs("-XstartOnFirstThread")
}

val projectVersion: String by project

tasks.jar {
    archiveFileName.set("$appName-$projectVersion.jar")
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    dependsOn(configurations.getByName("runtimeClasspath"))
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory()) it else zipTree(it) })

    exclude("META-INF/INDEX.LIST", "META-INF/*.SF", "META-INF/*.DSA", "META-INF/*.RSA")

    dependencies {
        exclude("META-INF/INDEX.LIST", "META-INF/maven/**")
    }

    manifest {
        attributes(mapOf("Main-Class" to mainClassName))
    }

    doLast {
        file(archiveFile).setExecutable(true, false)
    }
}

tasks.register<Jar>("jarMac") {
    dependsOn(tasks.named("jar"))
    group = "build"

    archiveFileName.set("$appName-$projectVersion-mac.jar")
    exclude(
        "windows/x86/**",
        "windows/x64/**",
        "linux/arm32/**",
        "linux/arm64/**",
        "linux/x64/**",
        "**/*.dll",
        "**/*.so",
        "META-INF/INDEX.LIST",
        "META-INF/*.SF",
        "META-INF/*.DSA",
        "META-INF/*.RSA",
    )
    dependencies {
        exclude(
            "windows/x86/**",
            "windows/x64/**",
            "linux/arm32/**",
            "linux/arm64/**",
            "linux/x64/**",
            "META-INF/INDEX.LIST",
            "META-INF/maven/**",
        )
    }
}

tasks.register<Jar>("jarLinux") {
    dependsOn("jar")
    group = "build"
    archiveFileName.set("$appName-$projectVersion-linux.jar")
    exclude(
        "windows/x86/**",
        "windows/x64/**",
        "macos/arm64/**",
        "macos/x64/**",
        "**/*.dll",
        "**/*.dylib",
        "META-INF/INDEX.LIST",
        "META-INF/*.SF",
        "META-INF/*.DSA",
        "META-INF/*.RSA",
    )
    dependencies {
        exclude(
            "windows/x86/**",
            "windows/x64/**",
            "macos/arm64/**",
            "macos/x64/**",
            "META-INF/INDEX.LIST",
            "META-INF/maven/**",
        )
    }
}

tasks.register<Jar>("jarWin") {
    dependsOn("jar")
    group = "build"
    archiveFileName.set("$appName-$projectVersion-win.jar")
    exclude(
        "macos/arm64/**",
        "macos/x64/**",
        "linux/arm32/**",
        "linux/arm64/**",
        "linux/x64/**",
        "**/*.dylib",
        "**/*.so",
        "META-INF/INDEX.LIST",
        "META-INF/*.SF",
        "META-INF/*.DSA",
        "META-INF/*.RSA",
    )
    dependencies {
        exclude(
            "macos/arm64/**",
            "macos/x64/**",
            "linux/arm32/**",
            "linux/arm64/**",
            "linux/x64/**",
            "META-INF/INDEX.LIST",
            "META-INF/maven/**",
        )
    }
}

construo {
    name.set(appName)
    humanName.set(appName)
    version.set(projectVersion)

    targets {
        register<Target.Linux>("linuxX64") {
            architecture.set(Target.Architecture.X86_64)
            jdkUrl.set(
                "https://github.com/adoptium/temurin17-binaries/releases/download/jdk-17.0.15%2B6/OpenJDK17U-jdk_x64_linux_hotspot_17.0.15_6.tar.gz",
            )
        }
        register<Target.MacOs>("macM1") {
            architecture.set(Target.Architecture.AARCH64)
            jdkUrl.set(
                "https://github.com/adoptium/temurin17-binaries/releases/download/jdk-17.0.15%2B6/OpenJDK17U-jdk_aarch64_mac_hotspot_17.0.15_6.tar.gz",
            )
            identifier.set("pro.piechowski.highschoolstory." + appName)
            macIcon.set(project.file("icons/logo.icns"))
        }
        register<Target.MacOs>("macX64") {
            architecture.set(Target.Architecture.X86_64)
            jdkUrl.set(
                "https://github.com/adoptium/temurin17-binaries/releases/download/jdk-17.0.15%2B6/OpenJDK17U-jdk_x64_mac_hotspot_17.0.15_6.tar.gz",
            )
            identifier.set("pro.piechowski.highschoolstory." + appName)
            macIcon.set(project.file("icons/logo.icns"))
        }
        register<Target.Windows>("winX64") {
            architecture.set(Target.Architecture.X86_64)
            icon.set(project.file("icons/logo.png"))
            jdkUrl.set(
                "https://github.com/adoptium/temurin17-binaries/releases/download/jdk-17.0.15%2B6/OpenJDK17U-jdk_x64_windows_hotspot_17.0.15_6.zip",
            )
        }
    }
}

tasks.register("dist") {
    dependsOn("jar")
}

distributions {
    main {
        contents {
            into("libs") {
                project.configurations.runtimeClasspath
                    .get()
                    .files
                    .filter { file ->
                        file.getName() !=
                            project.tasks.jar
                                .get()
                                .outputs.files.singleFile.name
                    }.forEach { file ->
                        exclude(file.name)
                    }
            }
        }
    }
}

tasks.named<CreateStartScripts>("startScripts") {
    dependsOn(":game:lwjgl3:jar")
    classpath =
        files(
            project.tasks
                .named("jar")
                .get()
                .outputs.files,
        )
}
