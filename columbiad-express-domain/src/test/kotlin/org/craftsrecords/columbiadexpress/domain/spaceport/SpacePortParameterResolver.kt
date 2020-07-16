package org.craftsrecords.columbiadexpress.domain.spaceport

import org.craftsrecords.TypedParameterResolver
import org.craftsrecords.columbiadexpress.domain.Random
import org.craftsrecords.columbiadexpress.domain.spaceport.AstronomicalBody.EARTH
import org.craftsrecords.columbiadexpress.domain.spaceport.AstronomicalBody.MOON

class SpacePortParameterResolver : TypedParameterResolver<SpacePort>({ parameterContext, _ ->
    when {
        parameterContext.isAnnotated(Random::class.java) && parameterContext.isAnnotated(OnEarth::class.java) -> {
            randomSpacePort(EARTH)
        }

        parameterContext.isAnnotated(Random::class.java) -> {
            randomSpacePort()
        }
        parameterContext.isAnnotated(OnEarth::class.java) -> {
            spacePort(EARTH)
        }
        parameterContext.isAnnotated(OnMoon::class.java) -> {
            spacePort(MOON)
        }

        else -> spacePort()
    }
})
