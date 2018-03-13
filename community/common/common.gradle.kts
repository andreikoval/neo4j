plugins {
    `java-library`
}

dependencies {
    implementation(Libs.findbugs_annotations)

    testImplementation(Libs.junit)
    testImplementation(Libs.hamcrest)
    testImplementation(Libs.mockito)
}

exposeTestJar()
