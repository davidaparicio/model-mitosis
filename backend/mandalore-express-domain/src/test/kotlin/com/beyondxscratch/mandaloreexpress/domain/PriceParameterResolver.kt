package com.beyondxscratch.mandaloreexpress.domain

import com.beyondxscratch.TypedParameterResolver

class PriceParameterResolver : TypedParameterResolver<Price>({ parameterContext, _ ->
    when {
        parameterContext.isAnnotated(Random::class.java) -> {
            randomPrice()
        }

        else -> price()

    }
})
