package eu.superbob.bfc

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class BrainfuckCode(val value: String)
