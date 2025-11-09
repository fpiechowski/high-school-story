plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.9.0"
}

include(":game:core", ":game:lwjgl3")

include(":engine:core")
include(":engine:inspector", ":engine:inspector:runtime", ":engine:inspector:plugin")
include(
    ":engine:gameplay:story",
    ":engine:gameplay:character",
    ":engine:gameplay:dialogue",
    ":engine:gameplay:interaction",
    ":engine:gameplay:scene",
    ":engine:gameplay:time",
    ":engine:gameplay:weather",
)
