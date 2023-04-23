package com.beyondxscratch.columbiadexpress.domain.search

import com.beyondxscratch.TypedParameterResolver
import com.beyondxscratch.columbiadexpress.domain.Random

class SpaceTrainParameterResolver : TypedParameterResolver<SpaceTrain>({ parameterContext, _ ->
    when {
        parameterContext.isAnnotated(Random::class.java) -> {
            randomSpaceTrain()
        }

        parameterContext.isAnnotated(Inbound::class.java) -> {
            inboundSpaceTrain()
        }

        else -> outboundSpaceTrain()
    }
})