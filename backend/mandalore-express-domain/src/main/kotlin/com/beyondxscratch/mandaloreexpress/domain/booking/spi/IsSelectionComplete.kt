package com.beyondxscratch.mandaloreexpress.domain.booking.spi

import java.util.UUID

interface IsSelectionComplete {
    infix fun of(searchId :UUID) : Boolean

}
