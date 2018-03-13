import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.process.internal.worker.GradleWorkerMain

plugins {
    `java-library`
}

dependencies {
    api(project(":community:graphdb-api"))
    api(Libs.netty)
    api(Libs.bouncycastle)
    implementation(project(":community:collections"))

    testImplementation(project(":community:io"))
    testImplementation(Libs.junit)
    testImplementation(Libs.hamcrest)
    testImplementation(Libs.mockito)
}
