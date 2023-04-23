package com.beyondxscratch.columbiadexpress.domain.search

import com.beyondxscratch.TypedParameterResolver
import com.beyondxscratch.columbiadexpress.domain.Random
import com.beyondxscratch.columbiadexpress.domain.search.criteria.Journey
import com.beyondxscratch.columbiadexpress.domain.search.criteria.journey
import com.beyondxscratch.columbiadexpress.domain.search.criteria.outboundJourney

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
