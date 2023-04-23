package com.beyondxscratch.columbiadexpress.domain.spaceport

import com.beyondxscratch.TypedParameterResolver
import com.beyondxscratch.columbiadexpress.domain.Random
import com.beyondxscratch.columbiadexpress.domain.spaceport.AstronomicalBody.EARTH
import com.beyondxscratch.columbiadexpress.domain.spaceport.AstronomicalBody.MOON

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
