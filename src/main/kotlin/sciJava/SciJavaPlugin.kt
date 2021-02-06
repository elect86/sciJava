/*
 * This Kotlin source file was generated by the Gradle 'init' task.
 */
package sciJava

import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.Plugin
import org.gradle.api.tasks.Input
import org.gradle.kotlin.dsl.create
import org.w3c.dom.Node
import org.xml.sax.InputSource
import java.io.StringReader
import java.net.URL
import javax.xml.parsers.DocumentBuilderFactory

val pom = getPom(base = false, version = "29.2.1")

val pomBase = getPom(base = true, version = "11.2.0")

fun getPom(base: Boolean, version: String): String {
    var name = "pom-scijava"
    if (base)
        name += "-base"
    val adr = "https://maven.scijava.org/content/groups/public/org/scijava/$name/$version/$name-$version.pom"
    return URL(adr).readText()
}

class SciJavaPluginExtension {
    var greeter = "Baeldung"
    var message = "Message from the plugin!"
    fun now() = println("now")
    // standard getters and setters
}

class SciJavaPlugin : Plugin<Project> {
    init {
        println("SciJavaPlugin::init")
    }
    override fun apply(project: Project) {
        println("SciJavaPlugin::apply")

        val extension = project.extensions.create<SciJavaPluginExtension>("sciJava")

        readKotlinVersion()
        fillDeps()
    }
}


fun readKotlinVersion() {

    val dbFactory = DocumentBuilderFactory.newInstance()
    val dBuilder = dbFactory.newDocumentBuilder()
    val doc = dBuilder.parse(InputSource(StringReader(pomBase)))

    //optional, but recommended
    //read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
    doc.documentElement.normalize()

    for (i in 0 until doc.documentElement.childNodes.length) {
        val child = doc.documentElement.childNodes.item(i)

        if (child.nodeName == "properties")

            for (j in 0 until child.childNodes.length) {
                val prop = child.childNodes.item(j)

                if (prop.nodeType == Node.ELEMENT_NODE && prop.nodeName == "kotlin.version")
                    versions["kotlin"] = prop.textContent
            }
    }
}

fun fillDeps() {

    val dbFactory = DocumentBuilderFactory.newInstance()
    val dBuilder = dbFactory.newDocumentBuilder()
    val doc = dBuilder.parse(InputSource(StringReader(pom)))

    //optional, but recommended
    //read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
    doc.documentElement.normalize()

    for (i in 0 until doc.documentElement.childNodes.length) {
        val child = doc.documentElement.childNodes.item(i)

        if (child.nodeName == "properties")

            for (j in 0 until child.childNodes.length) {
                val prop = child.childNodes.item(j)

                if (prop.nodeType == Node.ELEMENT_NODE && prop.nodeName.endsWith(".version")) {

                    val dep = prop.nodeName.dropLast(8)
                    val content = prop.textContent
                    versions[dep] = when {
                        content.startsWith("\${") && content.endsWith(".version}") -> { // ${imagej1.version}
                            val resolve = content.drop(2).dropLast(9)
                            versions[resolve] ?: error("cannot resolve $resolve")
                        }
                        else -> content
                    }
                }
            }
    }
}

val versions = mutableMapOf<String, String>()