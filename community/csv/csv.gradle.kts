import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.process.internal.worker.GradleWorkerMain

plugins {
    `java-library`
}

dependencies {
    api(project(":community:values"))
    implementation(project(":community:primitive-collections"))
    implementation(project(":community:collections"))
    implementation(Libs.findbugs_annotations)

    testImplementation(Libs.junit)
    testImplementation(Libs.hamcrest)
    testImplementation(Libs.commons_codec)
    testImplementation(project(":community:io"))
    testImplementation(project(":community:io", "tests"))
}
