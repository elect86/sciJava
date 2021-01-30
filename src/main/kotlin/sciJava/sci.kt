package sciJava

import org.gradle.api.Action
import org.gradle.api.artifacts.ExternalModuleDependency
import org.gradle.kotlin.dsl.DependencyHandlerScope
import sciJava.dsl.implementation
import sciJava.dsl.runtimeOnly
import sciJava.dsl.testImplementation
import sciJava.dsl.testRuntimeOnly

/**
 * com.github.scenerygraphics:vector:958f2e6
 * net.java.dev.jna:jna
 * net.java.dev.jna:jna-platform:${jna}
 */
var debug = false

fun DependencyHandlerScope.sciJava(deps: List<String>) {
    for (dep in deps)
        sciJava(dep)
}

fun DependencyHandlerScope.sciJava(dep: String, native: String? = null, test: Boolean = false,
                                   config: Action<ExternalModuleDependency>? = null) {
    var dep = dep
    val deps = dep.split(':')
    var vers: String = deps.last()
    when (deps.size) {
        3 -> {
            vers = deps[2]
            // org.scijava:ui-behaviour:2.0.2
            // net.java.dev.jna:jna-platform:\$jna
            if (vers.startsWith('$')) { // we need to prepare for later extraction
                vers = vers.substring(1)
                dep = dep.substringBeforeLast(':') // remove tail
            } else { // complete, eg: com.github.scenerygraphics:vector:958f2e6
                if (test) {
                    if (debug) println("testImplementation(\"$dep\")")
                    when (config) {
                        null -> testImplementation(dep)
                        else -> testImplementation(dep, config)
                    }
                } else {
                    if (debug) println("implementation(\"$dep\")")
                    when (config) {
                        null -> implementation(dep)
                        else -> implementation(dep, config)
                    }
                }
                if (native != null)
                    if (test) {
                        if (debug) println("testRuntimeOnly(${deps[0]}, ${deps[1]}, ${deps[2]}, classifier = $native)")
                        testRuntimeOnly(deps[0], deps[1], deps[2], classifier = native, dependencyConfiguration = config)
                    } else {
                        if (debug) println("runtimeOnly(${deps[0]}, ${deps[1]}, ${deps[2]}, classifier = $native)")
                        runtimeOnly(deps[0], deps[1], deps[2], classifier = native, dependencyConfiguration = config)
                    }
                return
            }
        }
        2 -> vers = deps[1]
        1 -> {
            // net.imagej
            vers = dep.substringAfterLast('.')
            dep = "$dep:$vers"
        }
    }

    // we need to extract the version
    val version = versions[vers] ?: versions[vers.substringBefore('-')]
    //    println("vers=$vers, version=$version")
    if (test) {
        if (debug) println("testImplementation(\"$dep:$version\")")
        when (config) {
            null -> testImplementation("$dep:$version")
            else -> testImplementation("$dep:$version", config)
        }
    } else {
        if (debug) println("implementation(\"$dep:$version\")")
        when (config) {
            null -> implementation("$dep:$version")
            else -> implementation("$dep:$version", config)
        }
    }
    if (native != null)
        sciJavaRuntimeOnly(dep, native, test, config)
}

fun DependencyHandlerScope.sciJavaRuntimeOnly(dep: String, classifier: String? = null, test: Boolean = false,
                                              config: Action<ExternalModuleDependency>? = null) {
    val group = dep.substringBefore(':')
    val name = dep.substringAfter(':')
    if (test) {
        if (debug) println("testRuntimeOnly($group, $name, classifier = $classifier)")
        testRuntimeOnly(group, name, classifier = classifier, dependencyConfiguration = config)
    } else {
        if (debug) println("runtimeOnly($group, $name, classifier = $classifier)")
        runtimeOnly(group, name, classifier = classifier, dependencyConfiguration = config)
    }
}

fun DependencyHandlerScope.testSciJava(dep: String, native: String? = null, config: Action<ExternalModuleDependency>? = null) =
        sciJava(dep, native, test = true, config = config)