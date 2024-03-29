package com.beyondxscratch.mandaloreexpress.domain.booking

import com.beyondxscratch.TypedParameterResolver
import com.beyondxscratch.mandaloreexpress.domain.Random

class BookingParameterResolver : TypedParameterResolver<Booking>({ parameterContext, _ ->
    when {
        parameterContext.isAnnotated(Random::class.java) -> {
            randomBooking()
        }

        parameterContext.isAnnotated(Finalized::class.java) -> {
            finalizedBooking()
        }

        parameterContext.isAnnotated(NonFinalized::class.java) -> {
            nonFinalizedBooking()
        }

        else -> booking()
    }
})