BFC
===

Build compiler
--------------

    ./gradlew build

It will build all the modules (including the GraalVM native image of the compiler).  
You can disable native image generation by removing _bfc-cli-native_ from _settings.gradle.kts_ includes.

Compile some Brainfuck to a Java class
--------------------------------------

    # Using the jar and a JVM
    java -jar bfc-cli/build/libs/bfc-cli-all.jar bfc-binder/src/test/resources/hello.bf
    
    # Using the native binary without the JVM
    bfc-cli-native/build/graal/bfc bfc-binder/src/test/resources/hello.bf

Run
---

    java Hello

Debug ProTips™
--------------

### Decompile a class file to bytecode instructions

Can be useful to perform diffs between an expected result and an actual result.

    javap -c -v Hello.class > Hello.log

Remaining TODO
--------------

 * Implement multiple input parameters binding in binder
 * Implement integral input parsing and/or output formatting in compiler
 * \[complex] handle mixed str/char/string in input parameter binding
 * Make sure input end-of-stream are correctly handled (return 0? throw exception?)
 * Make sure UTF-8 characters are handled correctly
