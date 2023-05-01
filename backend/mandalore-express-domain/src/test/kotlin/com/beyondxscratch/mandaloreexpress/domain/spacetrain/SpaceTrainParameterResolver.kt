package com.beyondxscratch.mandaloreexpress.domain.spacetrain

import com.beyondxscratch.TypedParameterResolver
import com.beyondxscratch.mandaloreexpress.domain.Random

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