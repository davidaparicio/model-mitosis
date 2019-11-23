package org.craftsrecords.columbiadexpress.domain.search

import org.craftsrecords.columbiadexpress.domain.Random
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.ParameterContext
import org.junit.jupiter.api.extension.support.TypeBasedParameterResolver

class FareParameterResolver : TypeBasedParameterResolver<Fare>() {
    override fun resolveParameter(parameterContext: ParameterContext, extensionContext: ExtensionContext): Fare {
        if (parameterContext.parameter.isAnnotationPresent(Random::class.java)) {
            return randomFare()
        }
        return fare()
    }
}
