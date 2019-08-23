import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("bfc.code-conventions")
    kotlin("jvm")
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(project(":bfc-lib"))
    implementation("org.ow2.asm:asm:${Versions.asm}")
    testImplementation("io.kotlintest:kotlintest-runner-junit5:${Versions.junitRunner}")
}
repositories {
    mavenCentral()
}
val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "1.8"
}
val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "1.8"
}