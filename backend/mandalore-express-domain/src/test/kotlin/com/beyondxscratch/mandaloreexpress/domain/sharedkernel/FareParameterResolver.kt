package com.beyondxscratch.mandaloreexpress.domain.sharedkernel

import com.beyondxscratch.TypedParameterResolver
import com.beyondxscratch.mandaloreexpress.domain.sharedkernel.fare.Fare

class FareParameterResolver : TypedParameterResolver<Fare>({ parameterContext, _ ->
    when {
        parameterContext.isAnnotated(Random::class.java) -> {
            randomFare()
        }
        else -> fare()
    }
})
