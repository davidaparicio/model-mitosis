package com.beyondxscratch.mandaloreexpress.domain.booking.tax

import com.beyondxscratch.TypedParameterResolver
import com.beyondxscratch.mandaloreexpress.domain.sharedkernel.OneCalamariFlan
import com.beyondxscratch.mandaloreexpress.domain.sharedkernel.OneRepCredit
import com.beyondxscratch.mandaloreexpress.domain.sharedkernel.Random
import com.beyondxscratch.mandaloreexpress.domain.sharedkernel.TenRepCredit

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