package com.beyondxscratch.mandaloreexpress.domain

import com.beyondxscratch.TypedParameterResolver
import com.beyondxscratch.mandaloreexpress.domain.search.criteria.Journey
import com.beyondxscratch.mandaloreexpress.domain.criteria.journey
import com.beyondxscratch.mandaloreexpress.domain.criteria.outboundJourney
import com.beyondxscratch.mandaloreexpress.domain.search.Search
import com.beyondxscratch.mandaloreexpress.domain.spacetrain.Outbound
import com.beyondxscratch.mandaloreexpress.domain.search.spacetrain.SpaceTrain
import com.beyondxscratch.mandaloreexpress.domain.spacetrain.outboundSpaceTrain
import com.beyondxscratch.mandaloreexpress.domain.spacetrain.spaceTrain

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
