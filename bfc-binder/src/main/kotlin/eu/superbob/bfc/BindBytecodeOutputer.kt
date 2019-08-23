@file:Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN")

package eu.superbob.bfc

import org.objectweb.asm.ClassWriter
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes.*
import org.objectweb.asm.Type
import java.io.InputStream
import java.io.OutputStream
import java.lang.reflect.Method
import java.lang.String as JavaLangString

//https://gongon95.wordpress.com/2015/07/14/asm%EC%9C%BC%EB%A1%9C-java-class-file-%EC%83%9D%EC%84%B1%ED%95%98%EA%B8%B0-2/
object BindBytecodeOutputer {
    fun compile(klass: Class<*>, classname: String): ByteArray =
            ClassWriter(ClassWriter.COMPUTE_FRAMES)
                    .apply { visitMainClass(classname, klass) }
                    .apply { visitMainClassConstructor() }
                    .apply { klass.methods.forEach { visitCustomMethod(classname, it) } }
                    .apply { visitIncreaseMethod(classname) }
                    .apply { visitDecreaseMethod(classname) }
                    .apply { visitGetMethod(classname) }
                    .apply { visitSetMethod(classname) }
                    .apply { visitLazyInitMemoryMethod() }
                    .apply { visitReadStrMethod() }
                    .apply { visitWriteStrMethod() }
                    .apply { visitReadIntMethod() }
                    .apply { visitWriteIntMethod() }
                    .apply { visitEnd() }
                    .toByteArray()
}

private fun ClassWriter.visitMainClass(classname: String, klass: Class<*>) {
    visit(
            V1_8,
            ACC_PUBLIC,
            classname,
            null,
            "java/lang/Object",
            arrayOf(Type.getType(klass).internalName))
}

private fun ClassWriter.visitMainClassConstructor() {
    visitMethod(ACC_PUBLIC, MethodDescriptors.Generated.constructor)
            .visitConstructorCode()
}

private fun ClassWriter.visitCustomMethod(classname: String, method: Method) {
    visitMethod(ACC_PUBLIC, method.name, Type.getMethodDescriptor(method), null, null)
            .visitCustomMethodCode(findSourceCode(method), classname, getInputBinding(method), getOutputBinding(method))
}

private fun getInputBinding(method: Method): InputBinding =
        when {
            method.parameterCount == 0 -> InputBinding.SYSTEM_IN
            method.parameterTypes[0].isAssignableFrom(InputStream::class.java) -> InputBinding.INPUTSTREAM
            method.parameterTypes[0].isAssignableFrom(JavaLangString::class.java) -> InputBinding.STRING
            method.parameterTypes[0].isAssignableFrom(Int::class.java) -> InputBinding.INT
            else -> error("Cannot find input binding for args")
        }

private fun getOutputBinding(method: Method): OutputBinding =
        when {
            method.parameterCount == 0  && method.returnType == Void.TYPE -> OutputBinding.SYSTEM_OUT
            method.parameterTypes.last().isAssignableFrom(OutputStream::class.java) -> OutputBinding.OUTPUTSTREAM
            method.returnType == Void.TYPE -> OutputBinding.SYSTEM_OUT
            method.returnType.isAssignableFrom(JavaLangString::class.java) -> OutputBinding.STRING
            method.returnType.isAssignableFrom(Int::class.java) -> OutputBinding.INT
            else -> error("Cannot find output binding for args/return type")
        }

private fun findSourceCode(method: Method): String =
        BrainfuckSourceLoader.loadSourceFor(method)

private fun ClassWriter.visitIncreaseMethod(classname: String) {
    visitMethod(ACC_PRIVATE + ACC_STATIC, MethodDescriptors.Generated.increase)
            .visitIncreaseMethodCode(classname)
}

private fun ClassWriter.visitDecreaseMethod(classname: String) {
    visitMethod(ACC_PRIVATE + ACC_STATIC, MethodDescriptors.Generated.decrease)
            .visitDecreaseMethodCode(classname)
}

private fun ClassWriter.visitGetMethod(classname: String) {
    visitMethod(ACC_PRIVATE + ACC_STATIC, MethodDescriptors.Generated.get)
            .visitGetMethodCode(classname)
}

private fun ClassWriter.visitSetMethod(classname: String) {
    visitMethod(ACC_PRIVATE + ACC_STATIC, MethodDescriptors.Generated.set)
            .visitSetMethodCode(classname)
}

private fun ClassWriter.visitLazyInitMemoryMethod() {
    visitMethod(ACC_PRIVATE + ACC_STATIC, MethodDescriptors.Generated.lazyInitMemory)
            .visitLazyInitMemoryMethodCode()
}

private fun ClassWriter.visitReadStrMethod() {
    visitMethod(ACC_PRIVATE + ACC_STATIC, MethodDescriptors.Generated.read)
            .visitReadStrMethodCode()
}

private fun ClassWriter.visitWriteStrMethod() {
    visitMethod(ACC_PRIVATE + ACC_STATIC, MethodDescriptors.Generated.write)
            .visitWriteStrMethodCode()
}

private fun ClassWriter.visitReadIntMethod() {
    visitMethod(ACC_PRIVATE + ACC_STATIC, MethodDescriptors.Generated.readi)
            .visitReadIntMethodCode()
}

private fun ClassWriter.visitWriteIntMethod() {
    visitMethod(ACC_PRIVATE + ACC_STATIC, MethodDescriptors.Generated.writei)
            .visitWriteIntMethodCode()
}

private fun ClassWriter.visitMethod(access: Int, methodDescriptor: MethodDescriptor): MethodVisitor =
        visitMethod(access, methodDescriptor.methodName, methodDescriptor.descriptor, null, null)
