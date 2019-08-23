plugins {
    id("bfc.cli-native-conventions")
}

graal {
    mainClass("eu.superbob.bfc.CompilerKt")
    outputName("bfc")
    option("--no-fallback")
}

dependencies {
    implementation(project(":bfc-cli"))
}