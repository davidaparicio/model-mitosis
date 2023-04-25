package com.beyondxscratch.mandaloreexpress.domain.search

import com.beyondxscratch.TypedParameterResolver
import com.beyondxscratch.mandaloreexpress.domain.Random
import com.beyondxscratch.mandaloreexpress.domain.search.criteria.Journey
import com.beyondxscratch.mandaloreexpress.domain.search.criteria.journey
import com.beyondxscratch.mandaloreexpress.domain.search.criteria.outboundJourney

class SearchParameterResolver : TypedParameterResolver<Search>({ parameterContext, _ ->
    when {
        parameterContext.isAnnotated(Random::class.java) -> {
            randomSearch()
        }
        parameterContext.isAnnotated(RoundTrip::class.java) -> {
            roundTripSearch()
        }

        else -> oneWaySearch()
    }
})

class LinkedJourneyAndSpaceTrainParameterResolver : TypedParameterResolver<Pair<Journey, SpaceTrain>>({ parameterContext, _ ->
    when {
        parameterContext.isAnnotated(Outbound::class.java) -> {
            outboundJourney() to outboundSpaceTrain()
        }
        else -> journey() to spaceTrain()
    }
})
