plugins {
    `java-library`
}

dependencies {
    implementation(Libs.commons_lang)
    implementation(Libs.asm)
    implementation(Libs.asm_analysis)
    implementation(Libs.asm_tree)
    implementation(Libs.asm_util)

    testImplementation(Libs.junit)
    testImplementation(Libs.hamcrest)
    testImplementation(Libs.mockito)
}
