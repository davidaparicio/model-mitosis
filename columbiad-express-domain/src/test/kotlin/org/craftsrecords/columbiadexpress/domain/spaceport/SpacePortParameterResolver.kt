package org.craftsrecords.columbiadexpress.domain.spaceport

import org.craftsrecords.TypedParameterResolver
import org.craftsrecords.columbiadexpress.domain.Random
import org.craftsrecords.columbiadexpress.domain.spaceport.AstronomicalBody.MOON

class SpacePortParameterResolver : TypedParameterResolver<SpacePort>({ parameterContext, _ ->
    when {
        parameterContext.isAnnotated(Random::class.java) -> {
            randomSpacePort()
        }
        parameterContext.isAnnotated(OnEarth::class.java) -> {
            spacePort(AstronomicalBody.EARTH)
        }
        parameterContext.isAnnotated(OnMoon::class.java) -> {
            spacePort(MOON)
        }

        else -> spacePort()
    }
})
