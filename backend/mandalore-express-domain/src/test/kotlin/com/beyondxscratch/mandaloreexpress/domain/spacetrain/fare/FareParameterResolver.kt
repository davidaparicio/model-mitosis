package com.beyondxscratch.mandaloreexpress.domain.spacetrain.fare

import com.beyondxscratch.TypedParameterResolver
import com.beyondxscratch.mandaloreexpress.domain.Random
import com.beyondxscratch.mandaloreexpress.domain.sharedkernel.fare.Fare

class FareParameterResolver : TypedParameterResolver<Fare>({ parameterContext, _ ->
    when {
        parameterContext.isAnnotated(Random::class.java) -> {
            randomFare()
        }
        else -> fare()
    }
})
