import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.process.internal.worker.GradleWorkerMain

plugins {
    `java-library`
}

exposeTestJar()

dependencies {
    api(project(":community:common"))
    api(project(":community:graphdb-api"))
    implementation(project(":community:unsafe"))
    implementation(project(":community:primitive-collections"))
    implementation(project(":community:collections"))
    implementation(Libs.commons_lang)

    testImplementation(Libs.junit)
    testImplementation(Libs.hamcrest)
    testImplementation(Libs.commons_codec)
    testImplementation(Libs.mockito)
    testImplementation(project(":community:common", "tests"))
}

