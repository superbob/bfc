package eu.superbob.bfc

import org.objectweb.asm.Type
import java.lang.Integer as JavaLangInteger

object Types {
    val ArrayDeque: Type = Type.getType(java.util.ArrayDeque::class.java)
    val ArrayList: Type = Type.getType(kotlin.collections.ArrayList::class.java)
    val ByteArray: Type = Type.getType(kotlin.ByteArray::class.java)
    val ByteArrayInputStream: Type = Type.getType(java.io.ByteArrayInputStream::class.java)
    val ByteArrayOutputStream: Type = Type.getType(java.io.ByteArrayOutputStream::class.java)
    val Charset: Type = Type.getType(java.nio.charset.Charset::class.java)
    val Deque: Type = Type.getType(java.util.Deque::class.java)
    val InputStream: Type = Type.getType(java.io.InputStream::class.java)
    val Integer: Type = Type.getType(JavaLangInteger::class.java)
    val IOException: Type = Type.getType(java.io.IOException::class.java)
    val List: Type = Type.getType(kotlin.collections.List::class.java)
    val Object: Type = Type.getType(java.lang.Object::class.java)
    val OutputStream: Type = Type.getType(java.io.OutputStream::class.java)
    val PrintStream: Type = Type.getType(java.io.PrintStream::class.java)
    val StandardCharsets: Type = Type.getType(java.nio.charset.StandardCharsets::class.java)
    val String: Type = Type.getType(kotlin.String::class.java)
    val StringArray: Type = Type.getType(Array<String>::class.java)
    val System: Type = Type.getType(java.lang.System::class.java)
    val UncheckedIOException: Type = Type.getType(java.io.UncheckedIOException::class.java)
}
