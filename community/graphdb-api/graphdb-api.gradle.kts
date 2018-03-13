plugins {
    `java-library`
}

dependencies {
    api(project(":community:common"))
    api(project(":community:resource"))
    implementation(project(":community:collections"))
    implementation(project(":community:primitive-collections"))

    testImplementation(Libs.junit)
    testImplementation(Libs.mockito)
}
