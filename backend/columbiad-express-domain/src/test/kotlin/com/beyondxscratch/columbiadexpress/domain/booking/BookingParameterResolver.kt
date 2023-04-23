package com.beyondxscratch.columbiadexpress.domain.booking

import com.beyondxscratch.TypedParameterResolver
import com.beyondxscratch.columbiadexpress.domain.Random

class BookingParameterResolver : TypedParameterResolver<Booking>({ parameterContext, _ ->
    when {
        parameterContext.isAnnotated(Random::class.java) -> {
            randomBooking()
        }

        else -> booking()
    }
})