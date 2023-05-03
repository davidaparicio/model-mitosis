package com.beyondxscratch.mandaloreexpress.domain.spacetrain.fare

import com.beyondxscratch.TypedParameterResolver
import com.beyondxscratch.mandaloreexpress.domain.Random

class DiscountParameterResolver : TypedParameterResolver<Discount>({ parameterContext, _ ->
    when {
        parameterContext.isAnnotated(Random::class.java) -> randomDiscount()

        parameterContext.isAnnotated(OneRepCredit::class.java) -> oneRepCreditDiscount()

        parameterContext.isAnnotated(TenRepCredit::class.java) -> tenRepCreditDiscount()


        else -> discount()
    }
})
