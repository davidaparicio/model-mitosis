package com.beyondxscratch.mandaloreexpress.domain.search

import com.beyondxscratch.TypedParameterResolver
import com.beyondxscratch.mandaloreexpress.domain.annotations.Random
import com.beyondxscratch.mandaloreexpress.domain.annotations.RoundTrip
import com.beyondxscratch.mandaloreexpress.domain.search.criteria.Journey
import com.beyondxscratch.mandaloreexpress.domain.search.criteria.journey
import com.beyondxscratch.mandaloreexpress.domain.search.criteria.outboundJourney
import com.beyondxscratch.mandaloreexpress.domain.annotations.Outbound
import com.beyondxscratch.mandaloreexpress.domain.search.spacetrain.SpaceTrain
import com.beyondxscratch.mandaloreexpress.domain.search.spacetrain.outboundSpaceTrain
import com.beyondxscratch.mandaloreexpress.domain.search.spacetrain.spaceTrain

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
