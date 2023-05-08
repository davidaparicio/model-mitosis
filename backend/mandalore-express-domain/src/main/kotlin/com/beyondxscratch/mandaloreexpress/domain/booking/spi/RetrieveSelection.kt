package com.beyondxscratch.mandaloreexpress.domain.booking.spi

import com.beyondxscratch.mandaloreexpress.domain.booking.spacetrain.SpaceTrain
import java.util.*

interface RetrieveSelection {
    infix fun from(searchId : UUID) : List<SpaceTrain>
}
