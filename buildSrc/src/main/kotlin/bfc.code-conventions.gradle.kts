import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions

plugins {
    kotlin("jvm")
    id("io.gitlab.arturbosch.detekt")
}

repositories {
    mavenCentral()
}

val requiredJvmTarget = JavaVersion.VERSION_1_8.toString()

val java8AndCompilerOptions: KotlinJvmOptions.() -> Unit = {
    // jsr305=strict: Null safety checks enforcement from JSR 305 @NotNull and @Nullable annotation
    // jvm-default=enable: Use Java 8 interface defaults instead of Java 7 compatible Kotlin defaults
    // Maybe not usefull, may remove
    freeCompilerArgs = listOf("-Xjsr305=strict", "-Xjvm-default=all")
    jvmTarget = requiredJvmTarget
}

tasks.test {
    useJUnitPlatform()
}

tasks.compileKotlin {
    kotlinOptions(java8AndCompilerOptions)
}

tasks.compileTestKotlin {
    kotlinOptions(java8AndCompilerOptions)
}

dependencies {
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
}

detekt {
    buildUponDefaultConfig = true
    config = files("${rootProject.projectDir}/config/detekt/detekt.yml")
}

tasks.detekt {
    jvmTarget = requiredJvmTarget

    reports {
        html.required.set(true)
        txt.required.set(false)
        xml.required.set(false)
    }
}
