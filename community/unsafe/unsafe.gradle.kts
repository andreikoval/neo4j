plugins {
    `java-library`
}

dependencies {
    api(project(":community:common"))

    testImplementation(Libs.junit)
    testImplementation(Libs.hamcrest)
    testImplementation(Libs.mockito)
}
