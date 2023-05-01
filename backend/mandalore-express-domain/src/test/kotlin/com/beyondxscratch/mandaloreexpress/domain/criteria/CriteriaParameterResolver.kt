package com.beyondxscratch.mandaloreexpress.domain.criteria

import com.beyondxscratch.TypedParameterResolver
import com.beyondxscratch.mandaloreexpress.domain.Random
import com.beyondxscratch.mandaloreexpress.domain.RoundTrip

class CriteriaParameterResolver : TypedParameterResolver<Criteria>({ parameterContext, _ ->
    when {
        parameterContext.isAnnotated(RoundTrip::class.java) -> roundTripCriteria()
        parameterContext.isAnnotated(Random::class.java) -> randomCriteria()
        else -> criteria()
    }

})