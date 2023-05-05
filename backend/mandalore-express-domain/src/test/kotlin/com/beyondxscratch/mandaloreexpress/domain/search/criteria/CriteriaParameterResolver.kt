package com.beyondxscratch.mandaloreexpress.domain.search.criteria

import com.beyondxscratch.TypedParameterResolver
import com.beyondxscratch.mandaloreexpress.domain.annotations.Random
import com.beyondxscratch.mandaloreexpress.domain.annotations.RoundTrip

class CriteriaParameterResolver : TypedParameterResolver<Criteria>({ parameterContext, _ ->
    when {
        parameterContext.isAnnotated(RoundTrip::class.java) -> roundTripCriteria()
        parameterContext.isAnnotated(Random::class.java) -> randomCriteria()
        else -> criteria()
    }

})