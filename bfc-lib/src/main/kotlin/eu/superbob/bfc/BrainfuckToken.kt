package eu.superbob.bfc

sealed class BrainfuckToken {
    companion object {
        fun parse(symbol: Char): BrainfuckToken {
            return when (symbol) {
                NextMemory.symbol -> NextMemory
                PreviousMemory.symbol -> PreviousMemory
                Increase.symbol -> Increase
                Decrease.symbol -> Decrease
                Write.symbol -> Write
                Read.symbol -> Read
                LoopStart.symbol -> LoopStart
                LoopEnd.symbol -> LoopEnd
                else -> Comment
            }
        }
    }
}

abstract class BrainfuckInstruction(val symbol: Char) : BrainfuckToken()

object NextMemory : BrainfuckInstruction('>')
object PreviousMemory : BrainfuckInstruction('<')
object Increase : BrainfuckInstruction('+')
object Decrease : BrainfuckInstruction('-')
object Write : BrainfuckInstruction('.')
object Read : BrainfuckInstruction(',')
object LoopStart : BrainfuckInstruction('[')
object LoopEnd : BrainfuckInstruction(']')

object Comment : BrainfuckToken()
