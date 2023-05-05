package com.beyondxscratch.mandaloreexpress.domain.search

import com.beyondxscratch.TypedParameterResolver
import com.beyondxscratch.mandaloreexpress.domain.annotations.Random
import com.beyondxscratch.mandaloreexpress.domain.booking.Booking

class BookingParameterResolver : TypedParameterResolver<Booking>({ parameterContext, _ ->
    when {
        parameterContext.isAnnotated(Random::class.java) -> {
            randomBooking()
        }

        else -> booking()
    }
})