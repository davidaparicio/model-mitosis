package com.beyondxscratch.mandaloreexpress.domain.search.criteria

import com.beyondxscratch.TypedParameterResolver
import com.beyondxscratch.mandaloreexpress.domain.annotations.Random
import com.beyondxscratch.mandaloreexpress.domain.annotations.RoundTrip
import com.beyondxscratch.mandaloreexpress.domain.annotations.Inbound
import com.beyondxscratch.mandaloreexpress.domain.annotations.Outbound

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

class JourneysParameterResolver : TypedParameterResolver<Journeys>({ parameterContext, _ ->
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
