plugins {
    id("bfc.code-conventions")
    application
}

val distAll = tasks.register<Jar>("distAll") {
    archiveBaseName.set("${project.name}-all")
    manifest {
        attributes(mapOf("Main-Class" to application.mainClass.get()))
    }
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
    with(tasks.jar.get() as CopySpec).apply { duplicatesStrategy = DuplicatesStrategy.INCLUDE }
}

tasks {
    "build" {
        dependsOn(distAll)
    }
}