package com.beyondxscratch.mandaloreexpress.domain.search.spacetrain.fare

import com.beyondxscratch.TypedParameterResolver
import com.beyondxscratch.mandaloreexpress.domain.Random

class FareParameterResolver : TypedParameterResolver<Fare>({ parameterContext, _ ->
    when {
        parameterContext.isAnnotated(Random::class.java) -> {
            randomFare()
        }
        else -> fare()
    }
})
