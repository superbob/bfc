package eu.superbob.bfc

import java.lang.reflect.InvocationHandler
import java.lang.reflect.Proxy
import kotlin.random.Random
import kotlin.random.nextUInt

object Binder {
    fun <T> bindInterpret(klass: Class<T>): T {
        val handler =  InvocationHandler { _, method, args ->
            val input =
                    if (method.parameterCount > 0 && method.parameterTypes[0] == String::class.java) args[0] as String
                    else ""
            val brainfuckCode = BrainfuckSourceLoader.loadSourceFor(method)
            Interpreter.interpret(brainfuckCode, input)
        }
        return klass.cast(Proxy.newProxyInstance(Thread.currentThread().contextClassLoader, arrayOf(klass), handler))
    }

    fun <T> bindCompile(klass: Class<T>): T {
        val classname = generateName(klass)
//        println("Compiling ${klass.name} to binary definition: $classname...")
        val binaryClassDefinition = BindBytecodeOutputer.compile(klass, classname)
//        Files.newOutputStream(Paths.get("$classname.class")).use { it.write(binaryClassDefinition) }
//        println("Binary definition of class was written to $classname.class...")
        return klass.cast(createInstance(classname, binaryClassDefinition))
    }

    private fun createInstance(classname: String, binaryClassDefinition: ByteArray): Any? {
        val classLoader = object : ClassLoader() {
            fun defineClass(name: String, b: ByteArray): Class<*> {
                return defineClass(name, b, 0, b.size)
            }
        }
        return classLoader.defineClass(classname, binaryClassDefinition).getDeclaredConstructor().newInstance()
    }

    // TODO should generated class be in the same package? is it?
    // TODO use a procedurally generated hash number instead of random UInt.
    // Should hash source code + brainfuck imported source, but source code is not loaded yet.
    private fun generateName(klass: Class<*>): String =
            "${klass.simpleName}\$bfc\$${generateRandomHexNumber()}"

    @Suppress("MagicNumber") // 16 is the number of hexadecimal numbers
    private fun generateRandomHexNumber() = Random.nextUInt().toString(16)
}
