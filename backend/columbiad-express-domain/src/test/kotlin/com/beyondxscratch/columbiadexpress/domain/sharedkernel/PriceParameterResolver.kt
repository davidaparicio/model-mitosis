package com.beyondxscratch.columbiadexpress.domain.sharedkernel

import com.beyondxscratch.TypedParameterResolver
import com.beyondxscratch.columbiadexpress.domain.Random

class PriceParameterResolver : TypedParameterResolver<Price>({ parameterContext, _ ->
    when {
        parameterContext.isAnnotated(Random::class.java) -> {
            randomPrice()
        }

        else -> price()

    }
})
