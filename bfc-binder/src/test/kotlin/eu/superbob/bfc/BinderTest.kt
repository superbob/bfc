package eu.superbob.bfc

import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.io.OutputStream
import java.nio.charset.StandardCharsets

class BinderTest : StringSpec({
    "interpreted hello world should display Hello World!" {
        Binder.bindInterpret(Bindable::class.java).hello() shouldBe "Hello World!\n"
    }

    "interpreted hello world in a resource should display Hello World!" {
        Binder.bindInterpret(Bindable::class.java).helloFile() shouldBe "Hello World!\n"
    }

    "interpreted read/write should display itself" {
        Binder.bindInterpret(Bindable::class.java).echo("A") shouldBe "A"
    }

    "interpreted echo ! should print !" {
        Binder.bindInterpret(Bindable::class.java).bang() shouldBe "!"
    }

    "compiled hello world should display Hello World!" {
        Binder.bindCompile(Bindable2::class.java).helloFile()
    }

    "compiled read/write in/out should display itself" {
        Binder.bindCompile(Bindable2::class.java).echoInOut(ByteArrayInputStream("A".toByteArray(StandardCharsets.UTF_8)), System.out)
    }

    "compiled read/write in should display itself" {
        Binder.bindCompile(Bindable2::class.java).echoIn(ByteArrayInputStream("A".toByteArray(StandardCharsets.UTF_8)))
    }

    "compiled read/write str/str should display itself" {
        Binder.bindCompile(Bindable2::class.java).echoStrStr("A") shouldBe "A"
    }

    "compiled read/write Int/Int should display itself" {
        Binder.bindCompile(Bindable2::class.java).echoIntInt(56) shouldBe 56
    }
})

interface Bindable {
    @BrainfuckCode("++++++++++[>+++++++>++++++++++>+++>+<<<<-]>++.>+.+++++++..+++.>++.<<+++++++++++++++.>.+++.------.--------.>+.>.")
    fun hello(): String

    @BrainfuckResource("hello.bf")
    fun helloFile(): String

    @BrainfuckCode(",.")
    fun echo(input: String): String

    @BrainfuckCode("+++++++++++++++++++++++++++++++++.")
    fun bang(): String
}

interface Bindable2 {
    @BrainfuckResource("hello.bf")
    fun helloFile()

    @BrainfuckCode(",.")
    fun echoInOut(input: InputStream, output: OutputStream)

    @BrainfuckCode(",.")
    fun echoIn(input: InputStream)

    @BrainfuckCode(",.")
    fun echoStrStr(str: String): String

    @BrainfuckCode(",.")
    fun echoIntInt(input: Int): Int
}
