package eu.superbob.bfc

import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import java.nio.charset.StandardCharsets

class MainBytecodeOutputerTest : StringSpec({
    " ! should display !" {
        val javaClass = compileAndLoadClass("+++++++++++++++++++++++++++++++++.")
        val result = withMockedStreams {
            javaClass.getMethod("main", Array<String>::class.java).invoke(null, emptyArray<String>())
        }
        result shouldBe "!"
    }

    "read/write should display itself" {
        val javaClass = compileAndLoadClass(",.")
        val result = withMockedStreams("A") {
            javaClass.getMethod("main", Array<String>::class.java).invoke(null, emptyArray<String>())
        }
        result shouldBe "A"
    }

    "hello world should display Hello World!" {
        val javaClass = compileAndLoadClass("++++++++++[>+++++++>++++++++++>+++>+<<<<-]>++.>+.+++++++..+++.>++.<<+++++++++++++++.>.+++.------.--------.>+.>.")
        val result = withMockedStreams {
            javaClass.getMethod("main", Array<String>::class.java).invoke(null, emptyArray<String>())
        }
        result shouldBe "Hello World!\n"
    }
})

private fun compileAndLoadClass(brainfuckCode: String): Class<*> {
    val bytecode = MainBytecodeOutputer.compile(brainfuckCode, "Main")
    //Files.newOutputStream(Paths.get("Main.class")).use { it.write(bytecode) }
    val classLoader = object : ClassLoader() {
        fun defineClass(name: String, b: ByteArray): Class<*> {
            return defineClass(name, b, 0, b.size)
        }
    }
    return classLoader.defineClass("Main", bytecode)
}

private fun withMockedStreams(input: String = "", closure: () -> Any?): String {
    var standardIn = System.`in`
    val standardOut = System.out
    System.setIn(ByteArrayInputStream(input.toByteArray(StandardCharsets.UTF_8)))
    val byteArrayOutputStream = ByteArrayOutputStream()
    System.setOut(PrintStream(byteArrayOutputStream))
    closure()
    System.setIn(standardIn)
    System.setOut(standardOut)
    return String(byteArrayOutputStream.toByteArray(), StandardCharsets.UTF_8)
}
