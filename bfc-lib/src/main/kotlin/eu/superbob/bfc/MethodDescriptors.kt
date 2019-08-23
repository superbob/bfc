package eu.superbob.bfc

import org.objectweb.asm.Type

data class MethodDescriptor(val className: String, val methodName: String, val descriptor: String)

const val CONSTRUCTOR_NAME = "<init>"
// A dummy name is used for generated class methods as the classname is dynamically generated and cannot be predicted
const val DUMMY_CLASS_NAME = "DummyName"

object MethodDescriptors {
    object Object {
        val constructor = MethodDescriptor(Types.Object.internalName, CONSTRUCTOR_NAME, Type.getMethodDescriptor(Type.VOID_TYPE))
    }

    object Generated {
        val constructor    = MethodDescriptor(DUMMY_CLASS_NAME, CONSTRUCTOR_NAME, Type.getMethodDescriptor(Type.VOID_TYPE))
        val main           = MethodDescriptor(DUMMY_CLASS_NAME, "main", Type.getMethodDescriptor(Type.VOID_TYPE, Types.StringArray))
        val run            = MethodDescriptor(DUMMY_CLASS_NAME, "run", Type.getMethodDescriptor(Type.VOID_TYPE))
        val set            = MethodDescriptor(DUMMY_CLASS_NAME,  "\$bfc\$set", Type.getMethodDescriptor(Type.VOID_TYPE, Types.List, Type.INT_TYPE, Type.INT_TYPE))
        val lazyInitMemory = MethodDescriptor(DUMMY_CLASS_NAME,  "\$bfc\$lazyInitMemory", Type.getMethodDescriptor(Type.VOID_TYPE, Types.List, Type.INT_TYPE))
        val increase       = MethodDescriptor(DUMMY_CLASS_NAME,  "\$bfc\$increase", Type.getMethodDescriptor(Type.VOID_TYPE, Types.List, Type.INT_TYPE))
        val decrease       = MethodDescriptor(DUMMY_CLASS_NAME,  "\$bfc\$decrease", Type.getMethodDescriptor(Type.VOID_TYPE, Types.List, Type.INT_TYPE))
        val get            = MethodDescriptor(DUMMY_CLASS_NAME,  "\$bfc\$get", Type.getMethodDescriptor(Type.INT_TYPE, Types.List, Type.INT_TYPE))
        val read           = MethodDescriptor(DUMMY_CLASS_NAME,  "\$bfc\$read", Type.getMethodDescriptor(Type.INT_TYPE, Types.InputStream))
        val write          = MethodDescriptor(DUMMY_CLASS_NAME,  "\$bfc\$write", Type.getMethodDescriptor(Type.VOID_TYPE, Types.OutputStream, Type.INT_TYPE))
        val readi          = MethodDescriptor(DUMMY_CLASS_NAME,  "\$bfc\$readi", Type.getMethodDescriptor(Type.INT_TYPE, Types.Deque))
        val writei         = MethodDescriptor(DUMMY_CLASS_NAME,  "\$bfc\$writei", Type.getMethodDescriptor(Type.VOID_TYPE, Types.List, Type.INT_TYPE))
    }

    object Integer {
        val valueOf  = MethodDescriptor(Types.Integer.internalName, "valueOf", Type.getMethodDescriptor(Types.Integer, Type.INT_TYPE))
        val intValue = MethodDescriptor(Types.Integer.internalName, "intValue", Type.getMethodDescriptor(Type.INT_TYPE))
    }

    object List {
        val set  = MethodDescriptor(Types.List.internalName, "set", Type.getMethodDescriptor(Types.Object, Type.INT_TYPE, Types.Object))
        val get  = MethodDescriptor(Types.List.internalName, "get", Type.getMethodDescriptor(Types.Object, Type.INT_TYPE))
        val add  = MethodDescriptor(Types.List.internalName, "add", Type.getMethodDescriptor(Type.BOOLEAN_TYPE, Types.Object))
        val size = MethodDescriptor(Types.List.internalName, "size", Type.getMethodDescriptor(Type.INT_TYPE))
    }

    object ArrayList {
        val constructor = MethodDescriptor(Types.ArrayList.internalName, CONSTRUCTOR_NAME, Type.getMethodDescriptor(Type.VOID_TYPE))
    }

    object UncheckedIOException {
        val constructor = MethodDescriptor(Types.UncheckedIOException.internalName, CONSTRUCTOR_NAME, Type.getMethodDescriptor(Type.VOID_TYPE, Types.IOException))
    }

    object String {
        val constructor = MethodDescriptor(Types.String.internalName, CONSTRUCTOR_NAME, Type.getMethodDescriptor(Type.VOID_TYPE, Types.ByteArray, Types.Charset))
        val getBytes    = MethodDescriptor(Types.String.internalName, "getBytes", Type.getMethodDescriptor(Types.ByteArray, Types.Charset))
    }

    object ByteArrayInputStream {
        val constructor = MethodDescriptor(Types.ByteArrayInputStream.internalName, CONSTRUCTOR_NAME, Type.getMethodDescriptor(Type.VOID_TYPE, Types.ByteArray))
    }

    object ByteArrayOutputStream {
        val constructor = MethodDescriptor(Types.ByteArrayOutputStream.internalName, CONSTRUCTOR_NAME, Type.getMethodDescriptor(Type.VOID_TYPE))
        val toByteArray = MethodDescriptor(Types.ByteArrayOutputStream.internalName, "toByteArray", Type.getMethodDescriptor(Types.ByteArray))
    }

    object OutputStream {
        val write = MethodDescriptor(Types.OutputStream.internalName, "write", Type.getMethodDescriptor(Type.VOID_TYPE, Type.INT_TYPE))
    }

    object InputStream {
        val read = MethodDescriptor(Types.InputStream.internalName, "read", Type.getMethodDescriptor(Type.INT_TYPE))
    }

    object Deque {
        val removeFirst = MethodDescriptor(Types.Deque.internalName, "removeFirst", Type.getMethodDescriptor(Types.Object))
        val addLast     = MethodDescriptor(Types.Deque.internalName, "addLast", Type.getMethodDescriptor(Type.VOID_TYPE, Types.Object))
    }

    object ArrayDeque {
        val constructor = MethodDescriptor(Types.ArrayDeque.internalName, CONSTRUCTOR_NAME, Type.getMethodDescriptor(Type.VOID_TYPE))
    }
}
