package org.craftsrecords.columbiadexpress.domain.search

import org.craftsrecords.TypedParameterResolver
import org.craftsrecords.columbiadexpress.domain.Random

class FareParameterResolver : TypedParameterResolver<Fare>({ parameterContext, _ ->
    when {
        parameterContext.isAnnotated(Random::class.java) -> {
            randomFare()
        }
        else -> fare()
    }
})
