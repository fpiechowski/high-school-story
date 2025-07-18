import org.graalvm.buildtools.gradle.dsl.GraalVMExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.JavaExec
import org.gradle.kotlin.dsl.extra
import org.gradle.kotlin.dsl.named
import java.io.File

class NativeImagePlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.plugins.apply("org.graalvm.buildtools.native")


        project.extensions.configure<GraalVMExtension>("graalvmNative") {
            binaries {
                named("main") {
                    imageName.set("appName")
                    mainClass.set("mainClassName")
                    buildArgs.add("-march=compatibility")
                    jvmArgs.add("-Dfile.encoding=UTF8")
                    sharedLibrary.set(false)
                    resources.autodetect()
                }
            }
        }

        project.tasks.named<JavaExec>("run") {
            doNotTrackState("Running the app should not be affected by Graal.")
        }

        project.tasks.named("generateResourcesConfigFile") {
            doFirst {
                val assetsFolder = File("${project.rootDir}/assets/")
                val lwjgl3 = project.project(":lwjgl3")
                val resFolder =
                    File("${lwjgl3.projectDir}/src/main/resources/META-INF/native-image/${lwjgl3.extra["appName"]}")
                resFolder.mkdirs()
                val resFile = File(resFolder, "resource-config.json")
                resFile.delete()
                resFile.appendText(
                    """{
                          "resources":{
                          "includes":[
                            {
                              "pattern": ".*(
                          """.trimIndent()
                )
                // This adds every filename in the assets/ folder to a pattern that adds those files as resources.

                project.fileTree(assetsFolder).forEach {
                    // The backslash-Q and backslash-E escape the start and end of a literal string, respectively.
                    resFile.appendText("\\\\Q${it.name}\\\\E|")
                }
                // We also match all of the window icon images this way and the font files that are part of libGDX.
                resFile.appendText(
                    """libgdx.+\\\\.png|lsans.+)"
                             }
                              ]},
                              "bundles":[]
                            }
                            """.trimIndent()
                )
            }
        }
    }
}
