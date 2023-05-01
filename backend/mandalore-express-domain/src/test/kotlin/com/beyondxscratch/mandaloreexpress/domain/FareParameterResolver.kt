package com.beyondxscratch.mandaloreexpress.domain

import com.beyondxscratch.TypedParameterResolver

class FareParameterResolver : TypedParameterResolver<Fare>({ parameterContext, _ ->
    when {
        parameterContext.isAnnotated(Random::class.java) -> {
            randomFare()
        }
        else -> fare()
    }
})
