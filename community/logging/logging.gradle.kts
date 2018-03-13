import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.process.internal.worker.GradleWorkerMain

plugins {
    `java-library`
}

exposeTestJar()

dependencies {
    api(project(":community:primitive-collections"))
    api(project(":community:io"))
    implementation(Libs.findbugs_annotations)

    testImplementation(Libs.junit)
    testImplementation(Libs.hamcrest)
    testImplementation(Libs.mockito)
    testImplementation(Libs.commons_codec)
    testImplementation(Libs.commons_lang)
    testImplementation(project(":community:collections"))
    testImplementation(project(":community:io"))
    testImplementation(project(":community:io", "tests"))
    testImplementation(project(":community:common"))
    testImplementation(project(":community:common", "tests"))
}

