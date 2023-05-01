package com.beyondxscratch.mandaloreexpress.domain

import com.beyondxscratch.mandaloreexpress.domain.spacetrain.SpaceTrain
import com.beyondxscratch.mandaloreexpress.domain.spacetrain.fare.Price
import java.util.UUID
import java.util.UUID.randomUUID


data class Booking(val id: UUID = randomUUID(), val spaceTrains: List<SpaceTrain>) {//TODO: Change Type of Collection

    val totalPrice: Price get() = spaceTrains.map { it.fares.first().price }.reduce(Price::plus)

    init {
        require(spaceTrains.isNotEmpty()) {
            "cannot book nothing"
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Booking

        return id == other.id
    }

    override fun hashCode(): Int = id.hashCode()
}