plugins {
    `java-library`
}

dependencies {
    api(project(":community:common"))
    api(project(":community:resource"))
    implementation(project(":community:unsafe"))

    testImplementation(Libs.junit)
    testImplementation(Libs.hamcrest)
    testImplementation(Libs.mockito)
}
