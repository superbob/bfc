package eu.superbob.bfc

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class BrainfuckResource(val value: String)
