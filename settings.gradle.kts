import de.fayard.refreshVersions.bootstrapRefreshVersions

        pluginManagement {
            resolutionStrategy {
                eachPlugin {
                    if(requested.id.id == "sciJava")
                        useModule("com.github.elect86:sciJava:7753b133")//.also { println("found") }
                }
            }
            repositories {
                gradlePluginPortal()
                maven("https://jitpack.io")
            }
        }

buildscript {
    repositories { gradlePluginPortal() }
    dependencies.classpath("de.fayard.refreshVersions:refreshVersions:0.9.7")
}

bootstrapRefreshVersions()

plugins {} // Optional

rootProject.name = "sciJava"

//gradle.rootProject {
//    //    group = "scenery"
//    version = "0.2.0-beta-9-SNAPSHOT"
//    description = "Scenery-backed 3D visualization package for ImageJ."
//}

include(
    "common",
    "deprecated",
    "launcher",
    "legacy",
    "mesh",
    "mesh-io",
    "notebook",
    "ops",
    "plugins-batch",
    "plugins-commands",
    "plugins-tools",
    "plugins-uploader-ssh",
    "plugins-uploader-webdav",
    "scripting",
    "ui-awt",
    "ui-swing",
    "updater"
)

