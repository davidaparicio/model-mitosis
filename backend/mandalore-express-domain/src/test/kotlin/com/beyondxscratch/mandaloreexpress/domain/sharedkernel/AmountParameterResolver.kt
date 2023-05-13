package com.beyondxscratch.mandaloreexpress.domain.sharedkernel

import com.beyondxscratch.TypedParameterResolver

class AmountParameterResolver : TypedParameterResolver<Amount>({ parameterContext, _ ->
    when {
        parameterContext.isAnnotated(Random::class.java) -> {
            randomAmount()
        }

        else -> amount()

    }
})
