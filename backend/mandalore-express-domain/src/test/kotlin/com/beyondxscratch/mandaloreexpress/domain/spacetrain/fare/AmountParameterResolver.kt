package com.beyondxscratch.mandaloreexpress.domain.spacetrain.fare

import com.beyondxscratch.TypedParameterResolver
import com.beyondxscratch.mandaloreexpress.domain.Random
import com.beyondxscratch.mandaloreexpress.domain.sharedkernel.fare.Amount

class AmountParameterResolver : TypedParameterResolver<Amount>({ parameterContext, _ ->
    when {
        parameterContext.isAnnotated(Random::class.java) -> {
            randomAmount()
        }

        else -> amount()

    }
})
