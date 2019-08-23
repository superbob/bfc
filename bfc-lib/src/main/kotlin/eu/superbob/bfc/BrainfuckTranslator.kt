package eu.superbob.bfc

import org.objectweb.asm.Label
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes.*
import java.util.*

private const val INPUT_LOCATION = 1
private const val OUTPUT_LOCATION = 2
private const val STORAGE_LOCATION = 3
private const val MEMORY_POINTER_LOCATION = 4

object BrainfuckTranslator {
    fun generateCode(
            code: String,
            classname: String,
            inputBinding: InputBinding = InputBinding.SYSTEM_IN,
            outputBinding: OutputBinding = OutputBinding.SYSTEM_OUT): (MethodVisitor) -> Unit = { mv ->
        val startLabels = ArrayDeque<Label>()
        val endLabels = ArrayDeque<Label>()
        val localMemoryOffset = computeLocalMemoryOffset(inputBinding, outputBinding)
        initializeInput(mv, localMemoryOffset, inputBinding)
        initializeOutput(mv, localMemoryOffset, outputBinding)
        initializeMemory(mv, localMemoryOffset)
        initializeMemoryPointer(mv, localMemoryOffset)
        code.forEach {
            when (BrainfuckToken.parse(it)) {
                Increase -> increase(mv, localMemoryOffset, classname)
                Decrease -> decrease(mv, localMemoryOffset, classname)
                NextMemory -> nextMemory(mv, localMemoryOffset)
                PreviousMemory -> previousMemory(mv, localMemoryOffset)
                Read -> read(mv, localMemoryOffset, classname, inputBinding)
                Write -> write(mv, localMemoryOffset, classname, outputBinding)
                LoopStart -> loopStart(mv, localMemoryOffset, startLabels, endLabels, classname)
                LoopEnd -> loopEnd(mv, startLabels, endLabels)
                is BrainfuckInstruction -> { throw IllegalStateException("Unexpected Brainfuck instruction $it") }
                Comment -> {}
            }
        }
        generateReturn(mv, localMemoryOffset, outputBinding)
    }

    private fun computeLocalMemoryOffset(intputBinding: InputBinding, outputBinding: OutputBinding): Int {
        return when(intputBinding) {
            InputBinding.SYSTEM_IN -> 0
            InputBinding.INPUTSTREAM -> 0
            InputBinding.STRING -> 1
            InputBinding.INT -> 1
            else -> error("Cannot compute offset for inputbinding")
        } + when(outputBinding) {
            OutputBinding.SYSTEM_OUT -> 0
            OutputBinding.OUTPUTSTREAM -> 0
            OutputBinding.STRING -> 0
            OutputBinding.INT -> 0
            else -> error("Cannot compute offset for outputbinding")
        }
    }

    private fun initializeMemory(mv: MethodVisitor, localMemoryOffset: Int) {
        mv.visitTypeInsn(NEW, Types.ArrayList.internalName)
        mv.visitInsn(DUP)
        mv.visitMethodInsn(INVOKESPECIAL, MethodDescriptors.ArrayList.constructor)
        mv.visitVarInsn(ASTORE, localMemoryOffset + STORAGE_LOCATION)
    }

    private fun initializeMemoryPointer(mv: MethodVisitor, localMemoryOffset: Int) {
        mv.visitInsn(ICONST_0)
        mv.visitVarInsn(ISTORE, localMemoryOffset + MEMORY_POINTER_LOCATION)
    }

    private fun initializeInput(mv: MethodVisitor, localMemoryOffset: Int, inputBinding: InputBinding) {
        when (inputBinding) {
            InputBinding.SYSTEM_IN -> {
                mv.visitFieldInsn(GETSTATIC, Types.System.internalName, "in", Types.InputStream.descriptor)
                mv.visitVarInsn(ASTORE, localMemoryOffset + INPUT_LOCATION)
            }
            InputBinding.STRING -> {
                mv.visitTypeInsn(NEW, Types.ByteArrayInputStream.internalName)
                mv.visitInsn(DUP)
                mv.visitVarInsn(ALOAD, INPUT_LOCATION)
                mv.visitFieldInsn(GETSTATIC, Types.StandardCharsets.internalName, "UTF_8", Types.Charset.descriptor)
                mv.visitMethodInsn(INVOKEVIRTUAL, MethodDescriptors.String.getBytes)
                mv.visitMethodInsn(INVOKESPECIAL, MethodDescriptors.ByteArrayInputStream.constructor)
                mv.visitVarInsn(ASTORE, localMemoryOffset + INPUT_LOCATION)
            }
            InputBinding.INT -> {
                mv.visitTypeInsn(NEW, Types.ArrayDeque.internalName)
                mv.visitInsn(DUP)
                mv.visitMethodInsn(INVOKESPECIAL, MethodDescriptors.ArrayDeque.constructor)
                mv.visitVarInsn(ASTORE, localMemoryOffset + INPUT_LOCATION)
                mv.visitVarInsn(ALOAD, localMemoryOffset + INPUT_LOCATION)
                mv.visitVarInsn(ILOAD, INPUT_LOCATION)
                mv.visitMethodInsn(INVOKESTATIC, MethodDescriptors.Integer.valueOf)
                mv.visitMethodInsn(INVOKEINTERFACE, MethodDescriptors.Deque.addLast)
            }
            InputBinding.INPUTSTREAM -> {}
            else -> TODO()
        }
    }

    private fun initializeOutput(mv: MethodVisitor, localMemoryOffset: Int, outputBinding: OutputBinding) {
        when (outputBinding) {
            OutputBinding.SYSTEM_OUT -> {
                mv.visitFieldInsn(GETSTATIC, Types.System.internalName, "out", Types.PrintStream.descriptor)
                mv.visitVarInsn(ASTORE, localMemoryOffset + OUTPUT_LOCATION)
            }
            OutputBinding.STRING -> {
                mv.visitTypeInsn(NEW, Types.ByteArrayOutputStream.internalName)
                mv.visitInsn(DUP)
                mv.visitMethodInsn(INVOKESPECIAL, MethodDescriptors.ByteArrayOutputStream.constructor)
                mv.visitVarInsn(ASTORE, localMemoryOffset + OUTPUT_LOCATION)
            }
            OutputBinding.INT -> {
                mv.visitTypeInsn(NEW, Types.ArrayList.internalName)
                mv.visitInsn(DUP)
                mv.visitMethodInsn(INVOKESPECIAL, MethodDescriptors.ArrayList.constructor)
                mv.visitVarInsn(ASTORE, localMemoryOffset + OUTPUT_LOCATION)
            }
            OutputBinding.OUTPUTSTREAM -> {}
            else -> TODO()
        }
    }

    // '+' instruction
    private fun increase(mv: MethodVisitor, localMemoryOffset: Int, classname: String) {
        mv.visitVarInsn(ALOAD, localMemoryOffset + STORAGE_LOCATION)
        mv.visitVarInsn(ILOAD, localMemoryOffset + MEMORY_POINTER_LOCATION)
        mv.visitMethodInsn(INVOKESTATIC, MethodDescriptors.Generated.increase, classname)
    }

    // '-' instruction
    private fun decrease(mv: MethodVisitor, localMemoryOffset: Int, classname: String) {
        mv.visitVarInsn(ALOAD, localMemoryOffset + STORAGE_LOCATION)
        mv.visitVarInsn(ILOAD, localMemoryOffset + MEMORY_POINTER_LOCATION)
        mv.visitMethodInsn(INVOKESTATIC, MethodDescriptors.Generated.decrease, classname)
    }

    // '>' instruction
    private fun nextMemory(mv: MethodVisitor, localMemoryOffset: Int) {
        mv.visitIincInsn(localMemoryOffset + MEMORY_POINTER_LOCATION, 1)
    }

    // '<' instruction
    private fun previousMemory(mv: MethodVisitor, localMemoryOffset: Int) {
        mv.visitIincInsn(localMemoryOffset + MEMORY_POINTER_LOCATION, -1)
    }

    // ',' instruction
    private fun read(mv: MethodVisitor, localMemoryOffset: Int, classname: String, inputBinding: InputBinding) {
        mv.visitVarInsn(ALOAD, localMemoryOffset + STORAGE_LOCATION)
        mv.visitVarInsn(ILOAD, localMemoryOffset + MEMORY_POINTER_LOCATION)
        mv.visitVarInsn(ALOAD, localMemoryOffset + INPUT_LOCATION)
        if (inputBinding == InputBinding.INT)
            mv.visitMethodInsn(INVOKESTATIC, MethodDescriptors.Generated.readi, classname)
        else
            mv.visitMethodInsn(INVOKESTATIC, MethodDescriptors.Generated.read, classname)
        mv.visitMethodInsn(INVOKESTATIC, MethodDescriptors.Generated.set, classname)
    }

    // '.' instruction
    private fun write(mv: MethodVisitor, localMemoryOffset: Int, classname: String, outputBinding: OutputBinding) {
        mv.visitVarInsn(ALOAD, localMemoryOffset + OUTPUT_LOCATION)
        readMemory(mv, localMemoryOffset, classname)
        if (outputBinding == OutputBinding.INT)
            mv.visitMethodInsn(INVOKESTATIC, MethodDescriptors.Generated.writei, classname)
        else
            mv.visitMethodInsn(INVOKESTATIC, MethodDescriptors.Generated.write, classname)
    }

    // '[' instruction
    private fun loopStart(
            mv: MethodVisitor,
            localMemoryOffset: Int,
            startLabels: ArrayDeque<Label>,
            endLabels: ArrayDeque<Label>,
            classname: String) {
        val startLabel = Label()
        val endLabel = Label()
        startLabels.push(startLabel)
        endLabels.push(endLabel)

        mv.visitLabel(startLabel)
        readMemory(mv, localMemoryOffset, classname)
        mv.visitJumpInsn(IFEQ, endLabel)
    }

    // ']' instruction
    private fun loopEnd(mv: MethodVisitor, startLabels: ArrayDeque<Label>, endLabels: ArrayDeque<Label>) {
        val startLabel = startLabels.pop()
        val endLabel = endLabels.pop()

        mv.visitJumpInsn(GOTO, startLabel)
        mv.visitLabel(endLabel)
    }

    private fun readMemory(mv: MethodVisitor, localMemoryOffset: Int, classname: String) {
        mv.visitVarInsn(ALOAD, localMemoryOffset + STORAGE_LOCATION)
        mv.visitVarInsn(ILOAD, localMemoryOffset + MEMORY_POINTER_LOCATION)
        mv.visitMethodInsn(INVOKESTATIC, MethodDescriptors.Generated.get, classname)
    }

    private fun generateReturn(mv: MethodVisitor, localMemoryOffset: Int, outputBinding: OutputBinding) {
        when (outputBinding) {
            OutputBinding.SYSTEM_OUT -> mv.visitInsn(RETURN)
            OutputBinding.OUTPUTSTREAM -> mv.visitInsn(RETURN)
            OutputBinding.STRING -> {
                mv.visitTypeInsn(NEW, Types.String.internalName)
                mv.visitInsn(DUP)
                mv.visitVarInsn(ALOAD, localMemoryOffset + OUTPUT_LOCATION)
                mv.visitMethodInsn(INVOKEVIRTUAL, MethodDescriptors.ByteArrayOutputStream.toByteArray)
                mv.visitFieldInsn(GETSTATIC, Types.StandardCharsets.internalName, "UTF_8", Types.Charset.descriptor)
                mv.visitMethodInsn(INVOKESPECIAL, MethodDescriptors.String.constructor)
                mv.visitInsn(ARETURN)
            }
            OutputBinding.INT -> {
                mv.visitVarInsn(ALOAD, localMemoryOffset + OUTPUT_LOCATION)
                mv.visitInsn(ICONST_0)
                mv.visitMethodInsn(INVOKEINTERFACE, MethodDescriptors.List.get)
                mv.visitTypeInsn(CHECKCAST, Types.Integer.internalName)
                mv.visitMethodInsn(INVOKEVIRTUAL, MethodDescriptors.Integer.intValue)
                mv.visitInsn(IRETURN)
            }
            else -> error("Output binding not handled")
        }
    }
}
