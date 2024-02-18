package com.beyondxscratch.mandaloreexpress.domain.booking.tax

import com.beyondxscratch.TypedParameterResolver
import com.beyondxscratch.mandaloreexpress.domain.Random
import com.beyondxscratch.mandaloreexpress.domain.money.OneCalamariFlan
import com.beyondxscratch.mandaloreexpress.domain.money.OneRepCredit
import com.beyondxscratch.mandaloreexpress.domain.money.TenRepCredit

class TaxPortionParameterResolver : TypedParameterResolver<TaxPortion>({ parameterContext, _ ->
    when {
        parameterContext.isAnnotated(Random::class.java) -> {
            randomTaxPortion()
        }

        parameterContext.isAnnotated(TenRepCredit::class.java) -> tenRepCreditsTaxPortion()
        parameterContext.isAnnotated(OneRepCredit::class.java) -> oneRepCreditTaxPortion()
        parameterContext.isAnnotated(OneCalamariFlan::class.java) -> oneCalamariFlanTaxPortion()

        else -> taxPortion()
    }
})