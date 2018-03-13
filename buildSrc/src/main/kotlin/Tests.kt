
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginConvention
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.get

fun Project.exposeTestJar() {
    configure<JavaPluginConvention> {
        val testJar  =  tasks.create("testJar", Jar::class.java) {
            classifier = "tests"
            dependsOn(tasks["testClasses"])
            from(sourceSets["test"].output)
        }
        configurations.create("tests")
        artifacts.add("tests", testJar)
    }
}
