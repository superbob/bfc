package eu.superbob.bfc

import org.objectweb.asm.Label
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes.*

fun MethodVisitor.visitConstructorCode() {
    visitCode()
    visitVarInsn(ALOAD, 0)
    visitMethodInsn(INVOKESPECIAL, MethodDescriptors.Object.constructor)
    visitInsn(RETURN)
    visitMaxs(0, 0)
    visitEnd()
}

fun MethodVisitor.visitMainMethodCode(classname: String) {
    visitCode()
    visitTypeInsn(NEW, classname)
    visitInsn(DUP)
    visitMethodInsn(INVOKESPECIAL, MethodDescriptors.Generated.constructor, classname)
    visitMethodInsn(INVOKESPECIAL, MethodDescriptors.Generated.run, classname)
    visitInsn(RETURN)
    visitMaxs(0, 0)
    visitEnd()
}

fun MethodVisitor.visitIncreaseMethodCode(classname: String) {
    visitCode()
    visitVarInsn(ALOAD, 0)
    visitVarInsn(ILOAD, 1)
    visitMethodInsn(INVOKESTATIC, MethodDescriptors.Generated.lazyInitMemory, classname)
    visitVarInsn(ALOAD, 0)
    visitVarInsn(ILOAD, 1)
    visitVarInsn(ALOAD, 0)
    visitVarInsn(ILOAD, 1)
    visitMethodInsn(INVOKEINTERFACE, MethodDescriptors.List.get)
    visitTypeInsn(CHECKCAST, Types.Integer.internalName)
    visitMethodInsn(INVOKEVIRTUAL, MethodDescriptors.Integer.intValue)
    visitInsn(ICONST_1)
    visitInsn(IADD)
    visitMethodInsn(INVOKESTATIC, MethodDescriptors.Integer.valueOf)
    visitMethodInsn(INVOKEINTERFACE, MethodDescriptors.List.set)
    visitInsn(POP)
    visitInsn(RETURN)
    visitMaxs(0, 0)
    visitEnd()
}

fun MethodVisitor.visitDecreaseMethodCode(classname: String) {
    visitCode()
    visitVarInsn(ALOAD, 0)
    visitVarInsn(ILOAD, 1)
    visitMethodInsn(INVOKESTATIC, MethodDescriptors.Generated.lazyInitMemory, classname)
    visitVarInsn(ALOAD, 0)
    visitVarInsn(ILOAD, 1)
    visitVarInsn(ALOAD, 0)
    visitVarInsn(ILOAD, 1)
    visitMethodInsn(INVOKEINTERFACE, MethodDescriptors.List.get)
    visitTypeInsn(CHECKCAST, Types.Integer.internalName)
    visitMethodInsn(INVOKEVIRTUAL, MethodDescriptors.Integer.intValue)
    visitInsn(ICONST_1)
    visitInsn(ISUB)
    visitMethodInsn(INVOKESTATIC, MethodDescriptors.Integer.valueOf)
    visitMethodInsn(INVOKEINTERFACE, MethodDescriptors.List.set)
    visitInsn(POP)
    visitInsn(RETURN)
    visitMaxs(0, 0)
    visitEnd()
}

fun MethodVisitor.visitGetMethodCode(classname: String) {
    visitCode()
    visitVarInsn(ALOAD, 0)
    visitVarInsn(ILOAD, 1)
    visitMethodInsn(INVOKESTATIC, MethodDescriptors.Generated.lazyInitMemory, classname)
    visitVarInsn(ALOAD, 0)
    visitVarInsn(ILOAD, 1)
    visitMethodInsn(INVOKEINTERFACE, MethodDescriptors.List.get)
    visitTypeInsn(CHECKCAST, Types.Integer.internalName)
    visitMethodInsn(INVOKEVIRTUAL, MethodDescriptors.Integer.intValue)
    visitInsn(IRETURN)
    visitMaxs(0, 0)
    visitEnd()
}

fun MethodVisitor.visitSetMethodCode(classname: String) {
    visitCode()
    visitVarInsn(ALOAD, 0)
    visitVarInsn(ILOAD, 1)
    visitMethodInsn(INVOKESTATIC, MethodDescriptors.Generated.lazyInitMemory, classname)
    visitVarInsn(ALOAD, 0)
    visitVarInsn(ILOAD, 1)
    visitVarInsn(ILOAD, 2)
    visitMethodInsn(INVOKESTATIC, MethodDescriptors.Integer.valueOf)
    visitMethodInsn(INVOKEINTERFACE, MethodDescriptors.List.set)
    visitInsn(POP)
    visitInsn(RETURN)
    visitMaxs(0, 0)
    visitEnd()
}

fun MethodVisitor.visitLazyInitMemoryMethodCode() {
    visitCode()
    val whileStart = Label()
    val whileEnd = Label()
    visitLabel(whileStart)
    visitVarInsn(ALOAD, 0)
    visitMethodInsn(INVOKEINTERFACE, MethodDescriptors.List.size)
    visitVarInsn(ILOAD, 1)
    visitJumpInsn(IF_ICMPGT, whileEnd)
    visitVarInsn(ALOAD, 0)
    visitInsn(ICONST_0)
    visitMethodInsn(INVOKESTATIC, MethodDescriptors.Integer.valueOf)
    visitMethodInsn(INVOKEINTERFACE, MethodDescriptors.List.add)
    visitInsn(POP)
    visitJumpInsn(GOTO, whileStart)
    visitLabel(whileEnd)
    visitInsn(RETURN)
    visitMaxs(0, 0)
    visitEnd()
}

fun MethodVisitor.visitReadStrMethodCode() {
    visitCode()
    val tryStart = Label()
    val tryEnd = Label()
    val catchStart = Label()
    visitLabel(tryStart)
    visitVarInsn(ALOAD, 0)
    visitMethodInsn(INVOKEVIRTUAL, MethodDescriptors.InputStream.read)
    visitLabel(tryEnd)
    visitInsn(IRETURN)
    visitLabel(catchStart)
    visitVarInsn(ASTORE, 1)
    visitTypeInsn(NEW, Types.UncheckedIOException.internalName)
    visitInsn(DUP)
    visitVarInsn(ALOAD, 1)
    visitMethodInsn(INVOKESPECIAL, MethodDescriptors.UncheckedIOException.constructor)
    visitInsn(ATHROW)
    visitTryCatchBlock(tryStart, tryEnd, catchStart, Types.IOException.internalName)
    visitMaxs(0, 0)
    visitEnd()
}

fun MethodVisitor.visitWriteStrMethodCode() {
    visitCode()
    val tryStart = Label()
    val tryEnd = Label()
    val catchStart = Label()
    val returnLabel = Label()
    visitLabel(tryStart)
    visitVarInsn(ALOAD, 0)
    visitVarInsn(ILOAD, 1)
    visitMethodInsn(INVOKEVIRTUAL, MethodDescriptors.OutputStream.write)
    visitLabel(tryEnd)
    visitJumpInsn(GOTO, returnLabel)
    visitLabel(catchStart)
    visitVarInsn(ASTORE, 2)
    visitTypeInsn(NEW, Types.UncheckedIOException.internalName)
    visitInsn(DUP)
    visitVarInsn(ALOAD, 2)
    visitMethodInsn(INVOKESPECIAL, MethodDescriptors.UncheckedIOException.constructor)
    visitInsn(ATHROW)
    visitLabel(returnLabel)
    visitInsn(RETURN)
    visitTryCatchBlock(tryStart, tryEnd, catchStart, Types.IOException.internalName)
    visitMaxs(0, 0)
    visitEnd()
}

fun MethodVisitor.visitReadIntMethodCode() {
    visitCode()
    visitVarInsn(ALOAD, 0)
    visitMethodInsn(INVOKEINTERFACE, MethodDescriptors.Deque.removeFirst)
    visitTypeInsn(CHECKCAST, Types.Integer.internalName)
    visitMethodInsn(INVOKEVIRTUAL, MethodDescriptors.Integer.intValue)
    visitInsn(IRETURN)
    visitMaxs(0, 0)
    visitEnd()
}

fun MethodVisitor.visitWriteIntMethodCode() {
    visitCode()
    visitVarInsn(ALOAD, 0)
    visitVarInsn(ILOAD, 1)
    visitMethodInsn(INVOKESTATIC, MethodDescriptors.Integer.valueOf)
    visitMethodInsn(INVOKEINTERFACE, MethodDescriptors.List.add)
    visitInsn(POP)
    visitInsn(RETURN)
    visitMaxs(0, 0)
    visitEnd()
}

fun MethodVisitor.visitRunMethodCode(code: String, classname: String) {
    visitCode()
    apply(BrainfuckTranslator.generateCode(code, classname))
    visitMaxs(0, 0)
    visitEnd()
}

fun MethodVisitor.visitCustomMethodCode(
        code: String, classname: String, inputBinding: InputBinding, outputBinding: OutputBinding) {
    visitCode()
    apply(BrainfuckTranslator.generateCode(code, classname, inputBinding, outputBinding))
    visitMaxs(0, 0)
    visitEnd()
}

fun MethodVisitor.visitMethodInsn(
        opcode: Int, methodDescriptor: MethodDescriptor, classname: String = methodDescriptor.className) {
    visitMethodInsn(
            opcode, classname, methodDescriptor.methodName, methodDescriptor.descriptor, opcode == INVOKEINTERFACE)
}
