package org.craftsrecords.columbiadexpress.domain.search.criteria

import org.craftsrecords.TypedParameterResolver
import org.craftsrecords.columbiadexpress.domain.Random

class JourneyParameterResolver : TypedParameterResolver<Journey>({ parameterContext, _ ->
    when {
        parameterContext.isAnnotated(Random::class.java) -> {
            randomJourney()
        }
        else -> journey()
    }
})

class JourneysParameterResolver : TypedParameterResolver<List<Journey>>({ parameterContext, _ ->
    when {
        parameterContext.isAnnotated(Random::class.java) -> {
            listOf(randomJourney())
        }
        else -> listOf(journey())
    }
})
