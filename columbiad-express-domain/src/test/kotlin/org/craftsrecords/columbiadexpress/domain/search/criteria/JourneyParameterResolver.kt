package org.craftsrecords.columbiadexpress.domain.search.criteria

import org.craftsrecords.columbiadexpress.domain.Random
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.ParameterContext
import org.junit.jupiter.api.extension.support.TypeBasedParameterResolver

class JourneyParameterResolver : TypeBasedParameterResolver<Journey>() {

    override fun resolveParameter(parameterContext: ParameterContext, extensionContext: ExtensionContext): Journey {
        if (parameterContext.isAnnotated(Random::class.java)) {
            return randomJourney()
        }
        return journey()
    }
}
