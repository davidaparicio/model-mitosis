package com.beyondxscratch.mandaloreexpress.domain

import com.beyondxscratch.TypedParameterResolver

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