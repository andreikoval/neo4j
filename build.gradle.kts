import org.gradle.api.tasks.testing.logging.TestExceptionFormat

plugins {
    base
}

allprojects {
    group = "org.neo4j"
    version = "3.4"

    repositories {
        mavenCentral()
    }
}

subprojects {
    tasks {
        withType<Test> {
            testLogging {
                exceptionFormat = TestExceptionFormat.FULL
                showStandardStreams = true
            }
        }
    }
}

//java {
//    sourceCompatibility = JavaVersion.VERSION_1_8
//    targetCompatibility = JavaVersion.VERSION_1_8
//}
//
//tasks {
//    withType<Test> {
//        testLogging {
//            exceptionFormat = TestExceptionFormat.FULL
//            showStandardStreams = true
//        }
//    }
//}
//

//tasks {
//    withType<Test> {
//        testLogging {
//            exceptionFormat = TestExceptionFormat.FULL
//            showStandardStreams = true
//        }
//        maxParallelForks = 8
//    }
//}
//
