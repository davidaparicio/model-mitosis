package com.beyondxscratch.mandaloreexpress.domain

import com.beyondxscratch.TypedParameterResolver

class BookingParameterResolver : TypedParameterResolver<Booking>({ parameterContext, _ ->
    when {
        parameterContext.isAnnotated(Random::class.java) -> {
            randomBooking()
        }

        else -> booking()
    }
})