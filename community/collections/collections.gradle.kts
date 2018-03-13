plugins {
    `java-library`
}

dependencies {
    api(project(":community:common"))
    api(project(":community:resource"))

    testImplementation(Libs.junit)
    testImplementation(Libs.hamcrest)
    testImplementation(Libs.mockito)
}
