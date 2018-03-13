import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.process.internal.worker.GradleWorkerMain

plugins {
    `java-library`
}

dependencies {
    api(project(":community:graphdb-api"))
    implementation(project(":community:collections"))

    testImplementation(Libs.junit)
    testImplementation(Libs.hamcrest)
}
