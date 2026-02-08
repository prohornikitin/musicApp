package afc.musicapp.domain

@RequiresOptIn(message = "This API is type-unsafe. Use with care")
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.PROPERTY, AnnotationTarget.FUNCTION)
annotation class TypeUnsafe