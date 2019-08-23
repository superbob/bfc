package eu.superbob.bfc

import java.io.StringReader
import java.util.*

object Interpreter {
    fun interpret(code: String, input: String = ""): String {
        val memory = MutableMemory()
        var memoryPointer = 0
        var codePointer = 0
        val loopBeginnings = ArrayDeque<Int>()
        val output = StringBuilder()
        val inputReader = StringReader(input)
        while (codePointer < code.length) {
            when (code[codePointer].toBrainfuckToken()) {
                NextMemory -> memoryPointer++
                PreviousMemory -> memoryPointer--
                Increase -> memory[memoryPointer]++
                Decrease -> memory[memoryPointer]--
                Write -> output.append(memory[memoryPointer].toChar())
                Read -> memory[memoryPointer] = inputReader.read()
                LoopStart -> {
                    if (loopFinished(memory, memoryPointer)) {
                        codePointer = findMatchingLoopEnd(code, codePointer)
                    } else {
                        loopBeginnings.push(codePointer)
                    }
                }
                LoopEnd -> {
                    codePointer = loopBeginnings.pop() - 1
                }
                is BrainfuckInstruction -> { throw IllegalStateException("Unexpected Brainfuck instruction ${code[codePointer]}") }
                Comment -> {}
            }
            codePointer++
        }
        return output.toString()
    }

    private fun loopFinished(memory: MutableMemory, memoryPointer: Int) = memory[memoryPointer] == 0

    private fun findMatchingLoopEnd(code: String, startPointer: Int): Int {
        var matchingEndPointer = startPointer
        var openLoops = 1
        while (openLoops > 0) {
            matchingEndPointer++
            if (code[matchingEndPointer] == '[') {
                openLoops++
            } else if (code[matchingEndPointer] == ']') {
                openLoops--
            }
        }
        return matchingEndPointer
    }

}

fun Char.toBrainfuckToken(): BrainfuckToken = BrainfuckToken.parse(this)
