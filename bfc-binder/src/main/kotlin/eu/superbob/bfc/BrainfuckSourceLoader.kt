package eu.superbob.bfc

import java.lang.reflect.Method
import java.nio.charset.StandardCharsets

object BrainfuckSourceLoader {
    fun loadSourceFor(method: Method): String =
            loadFromCodeAnnotation(method)
                    ?: loadFromResourceAnnotation(method)
                    ?: error("Cannot find a correct annotation on '${method.name}' to load Brainfuck code")

    private fun loadFromCodeAnnotation(method: Method) =
            method.getAnnotation(BrainfuckCode::class.java)?.value

    private fun loadFromResourceAnnotation(method: Method) =
            method.getAnnotation(BrainfuckResource::class.java)?.value?.readStringFromResource()

    private fun String.readStringFromResource() =
            Thread.currentThread().contextClassLoader.getResourceAsStream(this)
                    ?.use { String(it.readBytes(), StandardCharsets.UTF_8) }
                    ?: error("Cannot find '$this' in resources")
}
