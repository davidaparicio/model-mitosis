package org.craftsrecords

import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.ParameterContext
import org.junit.jupiter.api.extension.ParameterResolutionException
import org.junit.jupiter.api.extension.ParameterResolver
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import kotlin.reflect.KType
import kotlin.reflect.KTypeProjection
import kotlin.reflect.KVariance
import kotlin.reflect.full.allSupertypes
import kotlin.reflect.full.createType
import kotlin.reflect.full.isSubtypeOf

abstract class TypedParameterResolver<T>(private val resolver: (parameterContext: ParameterContext, extensionContext: ExtensionContext) -> T) : ParameterResolver {

    private val supportedParameterType: KType

    init {

        supportedParameterType = enclosedTypeOfParameterResolver() ?: throw RuntimeException()
    }

    override fun supportsParameter(parameterContext: ParameterContext, extensionContext: ExtensionContext): Boolean {
        return getParameterType(parameterContext)?.let { supportedParameterType.isSubtypeOf(it) }
                ?: false
    }

    @ExperimentalStdlibApi
    @Throws(ParameterResolutionException::class)
    override fun resolveParameter(parameterContext: ParameterContext, extensionContext: ExtensionContext): T {
        return resolver(parameterContext, extensionContext)
    }

    private fun getParameterType(parameterContext: ParameterContext): KType? = ktype(parameterContext.parameter.parameterizedType)

    private fun ktype(javaType: Type, typeArguments: List<KType> = emptyList()): KType =
            when (javaType) {
                is Class<*> ->
                    javaType.kotlin.createType(typeArguments.map { KTypeProjection(KVariance.INVARIANT, it) })
                is ParameterizedType ->
                    ktype(javaType.rawType, javaType.actualTypeArguments.map { ktype(it) })
                /* is WildcardType ->
                     toKotlinType(javaType.upperBounds[0], typeArguments), incomplete use case, it doesn't supports
                     lowerbound ? super Integer neither "star" ?.
                     In the case of single '?' there is no lowerbound and the upper is set to Object.
                     Need to deal with KTypeProjection Covariant etc. ?
                     */
                else ->
                    throw IllegalArgumentException("Cannot convert Java type: $javaType")
            }

    private fun enclosedTypeOfParameterResolver(): KType? {
        return this::class.allSupertypes
                .first { it.classifier == TypedParameterResolver::class }
                .arguments[0].type
    }
}