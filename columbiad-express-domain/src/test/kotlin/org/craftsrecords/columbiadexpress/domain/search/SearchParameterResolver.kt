package org.craftsrecords.columbiadexpress.domain.search

import org.craftsrecords.TypedParameterResolver
import org.craftsrecords.columbiadexpress.domain.Random
import org.craftsrecords.columbiadexpress.domain.search.criteria.Journey
import org.craftsrecords.columbiadexpress.domain.search.criteria.journey
import org.craftsrecords.columbiadexpress.domain.search.criteria.outboundJourney

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
