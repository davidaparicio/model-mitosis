package org.craftsrecords.columbiadexpress.domain.booking

import org.craftsrecords.TypedParameterResolver
import org.craftsrecords.columbiadexpress.domain.Random

class BookingParameterResolver : TypedParameterResolver<Booking>({ parameterContext, _ ->
    when {
        parameterContext.isAnnotated(Random::class.java) -> {
            randomBooking()
        }

        else -> booking()
    }
})