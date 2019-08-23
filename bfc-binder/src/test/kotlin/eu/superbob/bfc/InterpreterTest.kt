package eu.superbob.bfc

import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec

class InterpreterTest : StringSpec({
    "hello world should display Hello World!" {
        Interpreter.interpret("++++++++++[>+++++++>++++++++++>+++>+<<<<-]>++.>+.+++++++..+++.>++.<<+++++++++++++++.>.+++.------.--------.>+.>.") shouldBe "Hello World!\n"
    }

    "read/write should display itself" {
        Interpreter.interpret(",.", "A") shouldBe "A"
    }

    "echo ! should print !" {
        Interpreter.interpret("+++++++++++++++++++++++++++++++++.") shouldBe "!"
    }
})
