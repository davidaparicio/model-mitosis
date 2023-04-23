package com.beyondxscratch.columbiadexpress.domain.search.criteria

import com.beyondxscratch.TypedParameterResolver
import com.beyondxscratch.columbiadexpress.domain.Random
import com.beyondxscratch.columbiadexpress.domain.search.RoundTrip

class CriteriaParameterResolver : TypedParameterResolver<Criteria>({ parameterContext, _ ->
    when {
        parameterContext.isAnnotated(RoundTrip::class.java) -> roundTripCriteria()
        parameterContext.isAnnotated(Random::class.java) -> randomCriteria()
        else -> criteria()
    }

})