package org.craftsrecords.columbiadexpress.domain.search

import org.craftsrecords.columbiadexpress.domain.Random
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.ParameterContext
import org.junit.jupiter.api.extension.support.TypeBasedParameterResolver

class SpaceTrainParameterResolver : TypeBasedParameterResolver<SpaceTrain>() {
    override fun resolveParameter(parameterContext: ParameterContext, extensionContext: ExtensionContext): SpaceTrain {
        if (parameterContext.isAnnotated(Random::class.java)) {
            return randomSpaceTrain()
        }
        return spaceTrain()
    }

}