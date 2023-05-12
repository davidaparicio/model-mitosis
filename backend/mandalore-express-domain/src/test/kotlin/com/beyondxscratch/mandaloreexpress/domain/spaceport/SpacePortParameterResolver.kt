package com.beyondxscratch.mandaloreexpress.domain.spaceport

import com.beyondxscratch.TypedParameterResolver
import com.beyondxscratch.mandaloreexpress.domain.Random
import com.beyondxscratch.mandaloreexpress.domain.search.spaceport.Planet.CORUSCANT
import com.beyondxscratch.mandaloreexpress.domain.search.spaceport.Planet.MANDALORE
import com.beyondxscratch.mandaloreexpress.domain.search.spaceport.SpacePort

class SpacePortParameterResolver : TypedParameterResolver<SpacePort>({ parameterContext, _ ->
    when {
        parameterContext.isAnnotated(Random::class.java) && parameterContext.isAnnotated(OnCoruscant::class.java) -> {
            randomSpacePort(CORUSCANT)
        }

        parameterContext.isAnnotated(Random::class.java) -> {
            randomSpacePort()
        }
        parameterContext.isAnnotated(OnCoruscant::class.java) -> {
            spacePort(CORUSCANT)
        }
        parameterContext.isAnnotated(OnMandalore::class.java) -> {
            spacePort(MANDALORE)
        }

        else -> spacePort()
    }
})
