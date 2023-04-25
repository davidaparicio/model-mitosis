package com.beyondxscratch.mandaloreexpress.domain.spaceport

import com.beyondxscratch.TypedParameterResolver
import com.beyondxscratch.mandaloreexpress.domain.Random
import com.beyondxscratch.mandaloreexpress.domain.spaceport.AstronomicalBody.EARTH
import com.beyondxscratch.mandaloreexpress.domain.spaceport.AstronomicalBody.MOON

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
