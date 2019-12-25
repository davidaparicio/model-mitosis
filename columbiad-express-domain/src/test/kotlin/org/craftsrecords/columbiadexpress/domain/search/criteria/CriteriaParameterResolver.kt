package org.craftsrecords.columbiadexpress.domain.search.criteria

import org.craftsrecords.TypedParameterResolver
import org.craftsrecords.columbiadexpress.domain.Random

class CriteriaParameterResolver : TypedParameterResolver<Criteria>({ parameterContext, _ ->
    when {
        parameterContext.isAnnotated(RoundTrip::class.java) -> roundTripCriteria()
        parameterContext.isAnnotated(Random::class.java) -> randomCriteria()
        else -> criteria()
    }

})