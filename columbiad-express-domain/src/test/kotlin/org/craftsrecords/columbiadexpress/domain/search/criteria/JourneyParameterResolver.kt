package org.craftsrecords.columbiadexpress.domain.search.criteria

import org.craftsrecords.TypedParameterResolver
import org.craftsrecords.columbiadexpress.domain.Random
import org.craftsrecords.columbiadexpress.domain.search.Inbound
import org.craftsrecords.columbiadexpress.domain.search.Outbound

class JourneyParameterResolver : TypedParameterResolver<Journey>({ parameterContext, _ ->
    when {
        parameterContext.isAnnotated(Random::class.java) -> {
            randomJourney()
        }
        parameterContext.isAnnotated(Outbound::class.java) -> {
            outboundJourney()
        }
        parameterContext.isAnnotated(Inbound::class.java) -> {
            inboundJourney()
        }
        else -> journey()
    }
})

class JourneysParameterResolver : TypedParameterResolver<List<Journey>>({ parameterContext, _ ->
    when {
        parameterContext.isAnnotated(Random::class.java) -> {
            listOf(randomJourney())
        }
        parameterContext.isAnnotated(RoundTrip::class.java) -> {
            listOf(journey(), inboundOf(journey()))
        }
        else -> listOf(journey())
    }
})
