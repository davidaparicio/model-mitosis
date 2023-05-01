package com.beyondxscratch.mandaloreexpress.domain.spacetrain

import com.beyondxscratch.TypedParameterResolver
import com.beyondxscratch.mandaloreexpress.domain.Random

class PriceParameterResolver : TypedParameterResolver<Price>({ parameterContext, _ ->
    when {
        parameterContext.isAnnotated(Random::class.java) -> {
            randomPrice()
        }

        else -> price()

    }
})
