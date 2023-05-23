package com.beyondxscratch.mandaloreexpress.domain.sharedkernel.fare

import com.beyondxscratch.TypedParameterResolver
import com.beyondxscratch.mandaloreexpress.domain.search.spacetrain.fare.Fare
import com.beyondxscratch.mandaloreexpress.domain.sharedkernel.Random

class FareParameterResolver : TypedParameterResolver<Fare>({ parameterContext, _ ->
    when {
        parameterContext.isAnnotated(Random::class.java) -> {
            randomFare()
        }
        else -> fare()
    }
})
