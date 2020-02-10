package org.craftsrecords.columbiadexpress.domain.search.criteria

import org.craftsrecords.TypedParameterResolver
import org.craftsrecords.columbiadexpress.domain.Random
import org.craftsrecords.columbiadexpress.domain.search.RoundTrip

class CriteriaParameterResolver : TypedParameterResolver<Criteria>({ parameterContext, _ ->
    when {
        parameterContext.isAnnotated(RoundTrip::class.java) -> roundTripCriteria()
        parameterContext.isAnnotated(Random::class.java) -> randomCriteria()
        else -> criteria()
    }

})