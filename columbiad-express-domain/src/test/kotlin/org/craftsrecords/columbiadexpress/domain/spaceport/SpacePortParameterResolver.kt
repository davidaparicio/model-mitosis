package org.craftsrecords.columbiadexpress.domain.spaceport

import org.craftsrecords.columbiadexpress.domain.Random
import org.craftsrecords.columbiadexpress.domain.spaceport.AstronomicalBody.EARTH
import org.craftsrecords.columbiadexpress.domain.spaceport.AstronomicalBody.MOON
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.ParameterContext
import org.junit.jupiter.api.extension.support.TypeBasedParameterResolver

class SpacePortParameterResolver : TypeBasedParameterResolver<SpacePort>() {
    override fun resolveParameter(parameterContext: ParameterContext, extensionContext: ExtensionContext): SpacePort {
        if (parameterContext.isAnnotated(Random::class.java)) {
            return randomSpacePort()
        }

        if (parameterContext.isAnnotated(OnEarth::class.java)) {
            return spacePort(EARTH)
        }

        if (parameterContext.isAnnotated(OnMoon::class.java)) {
            return spacePort(MOON)
        }
        return spacePort()
    }
}
