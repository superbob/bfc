package eu.superbob.bfc

import org.objectweb.asm.ClassWriter
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes.*

//https://gongon95.wordpress.com/2015/07/14/asm%EC%9C%BC%EB%A1%9C-java-class-file-%EC%83%9D%EC%84%B1%ED%95%98%EA%B8%B0-2/
object MainBytecodeOutputer {
    fun
            compile(code: String, classname: String): ByteArray =
            ClassWriter(ClassWriter.COMPUTE_FRAMES)
                    .apply { visitMainClass(classname) }
                    .apply { visitMainClassConstructor() }
                    .apply { visitMainMethod(classname) }
                    .apply { visitRunMethod(code, classname) }
                    .apply { visitIncreaseMethod(classname) }
                    .apply { visitDecreaseMethod(classname) }
                    .apply { visitGetMethod(classname) }
                    .apply { visitSetMethod(classname) }
                    .apply { visitLazyInitMemoryMethod() }
                    .apply { visitReadStrMethod() }
                    .apply { visitWriteStrMethod() }
                    //.apply { visitReadIntMethod() } // TODO Int methods are currently useless in MainBytecodeOutputter because it doesn't handle int types
                    //.apply { visitWriteIntMethod() }
                    .apply { visitEnd() }
                    .toByteArray()
}

private fun ClassWriter.visitMainClass(classname: String) {
    visit(
            V1_8,
            ACC_PUBLIC,
            classname,
            null,
            "java/lang/Object",
            null)
}

private fun ClassWriter.visitMainClassConstructor() {
    visitMethod(ACC_PUBLIC, MethodDescriptors.Generated.constructor)
            .visitConstructorCode()
}

private fun ClassWriter.visitMainMethod(classname: String) {
    visitMethod(ACC_PUBLIC + ACC_STATIC, MethodDescriptors.Generated.main)
            .visitMainMethodCode(classname)
}

private fun ClassWriter.visitRunMethod(code: String, classname: String) {
    visitMethod(ACC_PRIVATE, MethodDescriptors.Generated.run)
            .visitRunMethodCode(code, classname)
}

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
