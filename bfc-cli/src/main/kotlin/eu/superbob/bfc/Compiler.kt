package eu.superbob.bfc

import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths

object Compiler {
    fun compile(file: String) {
        val filePath = Paths.get(file)
        val classname = classnameFromFile(filePath.fileName.toString())
        val code = String(Files.newInputStream(filePath).use { it.readBytes() }, StandardCharsets.UTF_8)
        val compiledBytes = MainBytecodeOutputer.compile(code, classname)
        Files.newOutputStream(Paths.get("$classname.class")).use { it.write(compiledBytes) }
    }

    private fun classnameFromFile(fileName: String): String = fileName.removeExtensionIfAny().toCamelCase()

    private fun String.removeExtensionIfAny() =
            if (endsWith(".bf") || endsWith(".b")) substring(0, lastIndexOf(".")) else this

    private fun String.toCamelCase() = split(".", "-").joinToString("") { it.titlecaseFirstChar() }
    private fun String.titlecaseFirstChar() = replaceFirstChar(Char::titlecase)
}

fun main(args: Array<String>) {
    val filename = if (args.size == 1) args[0] else throw error("Wrong number of arguments: ${args.size}, 1 expected.")
    Compiler.compile(filename)
}
