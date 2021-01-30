package sciJava

import org.gradle.api.artifacts.ModuleDependency
import org.gradle.kotlin.dsl.exclude
import org.gradle.nativeplatform.platform.internal.DefaultNativePlatform

val is64: Boolean = run {
    val arch = System.getenv("PROCESSOR_ARCHITECTURE")
    val wow64Arch = System.getenv("PROCESSOR_ARCHITEW6432")
    arch?.endsWith("64") == true || wow64Arch?.endsWith("64") == true
}

val os = DefaultNativePlatform.getCurrentOperatingSystem()

val joglNative = "natives-" + when {
    os.isWindows -> "windows-amd64".also { check(is64) }
    os.isLinux -> "linux-" + if (is64) "i586" else "amd64"
    os.isMacOsX -> "macosx-universal"
    else -> error("invalid")
}

val lwjglNatives = "natives-" + when {
    os.isWindows -> "windows"
    os.isLinux -> "linux"
    os.isMacOsX -> "macos"
    else -> error("invalid")
}

val ffmpegNatives = when {
    os.isWindows -> "windows"
    os.isLinux -> "linux"
    os.isMacOsX -> "macosx"
    else -> error("invalid")
} + "-x86_64"


operator fun String.invoke(version: String) {
    versions[this] = version
}

operator fun String.get(vararg artifacts: String): List<String> = artifacts.map { "$this${if (':' in this) it else ":$it"}" }

//operator fun String.unaryPlus() = 0

//fun ModuleDependency.exclude(deps: List<String>): ModuleDependency {
//    for (dep in deps) {
//        val d = dep.split(':')
//        check(d.size == 2)
//        exclude(d[0], d[1])
//    }
//    return this
//}

//
//
//fun <T : ModuleDependency> T.exclude(group0: String?, module0: String?,
//                                     group1: String?, module1: String?): T =
//        uncheckedCast(exclude(mapOfNonNullValuesOf(
//                "group" to group0, "module" to module0,
//                "group" to group1, "module" to module1)))
//
//fun <T : ModuleDependency> T.exclude(group0: String?, module0: String?,
//                                     group1: String?, module1: String?,
//                                     group2: String?, module2: String?): T =
//        uncheckedCast(exclude(mapOfNonNullValuesOf(
//                "group" to group0, "module" to module0,
//                "group" to group1, "module" to module1,
//                "group" to group2, "module" to module2)))
//
//@Suppress("unchecked_cast", "nothing_to_inline")
//internal
//inline fun <T> uncheckedCast(obj: Any?): T = obj as T
//
//internal
//fun excludeMapFor(group: String?, module: String?): Map<String, String> =
//        mapOfNonNullValuesOf("group" to group, "module" to module)
//
//
//internal
//fun mapOfNonNullValuesOf(vararg entries: Pair<String, String?>): Map<String, String> =
//        mutableMapOf<String, String>().apply { for ((k, v) in entries) if (v != null) put(k, v) }
