package com.beyondxscratch.columbiadexpress.domain.booking

import com.beyondxscratch.TypedParameterResolver
import com.beyondxscratch.columbiadexpress.domain.Random

class SpaceTrainParameterResolver : TypedParameterResolver<SpaceTrain>({ parameterContext, _ ->
    when {
        parameterContext.isAnnotated(Random::class.java) -> {
            randomSpaceTrain()
        }

        else -> spaceTrain()
    }
})