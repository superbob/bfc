plugins {
    id("com.palantir.graal")
}

repositories {
    mavenCentral()
}

// Don't forget to install libz. On Ubuntu based systems: 'sudo apt-get install libz-dev'
tasks {
    "build" {
        dependsOn("nativeImage")
    }
}
