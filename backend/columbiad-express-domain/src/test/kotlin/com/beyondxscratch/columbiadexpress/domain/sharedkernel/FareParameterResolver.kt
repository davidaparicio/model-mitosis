package com.beyondxscratch.columbiadexpress.domain.sharedkernel

import com.beyondxscratch.TypedParameterResolver
import com.beyondxscratch.columbiadexpress.domain.Random

class FareParameterResolver : TypedParameterResolver<Fare>({ parameterContext, _ ->
    when {
        parameterContext.isAnnotated(Random::class.java) -> {
            randomFare()
        }
        else -> fare()
    }
})
