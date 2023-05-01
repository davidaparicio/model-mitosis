package com.beyondxscratch.mandaloreexpress.domain.criteria

import com.beyondxscratch.TypedParameterResolver
import com.beyondxscratch.mandaloreexpress.domain.Inbound
import com.beyondxscratch.mandaloreexpress.domain.Journey
import com.beyondxscratch.mandaloreexpress.domain.Journeys
import com.beyondxscratch.mandaloreexpress.domain.Outbound
import com.beyondxscratch.mandaloreexpress.domain.Random
import com.beyondxscratch.mandaloreexpress.domain.RoundTrip

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
