package com.beyondxscratch.mandaloreexpress.domain.search.spacetrain.fare

import com.beyondxscratch.TypedParameterResolver
import com.beyondxscratch.mandaloreexpress.domain.sharedkernel.Random
import com.beyondxscratch.mandaloreexpress.domain.sharedkernel.OneCalamariFlan
import com.beyondxscratch.mandaloreexpress.domain.sharedkernel.OneRepCredit
import com.beyondxscratch.mandaloreexpress.domain.sharedkernel.TenRepCredit
import com.beyondxscratch.mandaloreexpress.domain.sharedkernel.fare.Price

class PriceParameterResolver : TypedParameterResolver<Price>({ parameterContext, _ ->
    when {
        parameterContext.isAnnotated(Random::class.java) -> {
            randomPrice()
        }

        parameterContext.isAnnotated(TenRepCredit::class.java) -> tenRepCreditsPrice()
        parameterContext.isAnnotated(OneRepCredit::class.java) -> oneRepCreditPrice()
        parameterContext.isAnnotated(OneCalamariFlan::class.java) -> oneCalamariFlanPrice()

        else -> price()

    }
})
