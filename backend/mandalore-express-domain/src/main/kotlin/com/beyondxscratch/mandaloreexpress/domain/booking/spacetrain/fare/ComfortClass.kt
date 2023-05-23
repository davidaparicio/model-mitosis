package com.beyondxscratch.mandaloreexpress.domain.booking.spacetrain.fare

enum class ComfortClass(val compatibleSeatLocations: List<SeatLocation>) {
    FIRST(
        listOf(
            SeatLocation.ANY,
            SeatLocation.FLYING_BRIDGE,
            SeatLocation.PRIVATE_CABIN,
            SeatLocation.OBSERVATION_DECK
        )
    ),
    SECOND(
        listOf(
            SeatLocation.ANY,
            SeatLocation.CARGO_BAY,
            SeatLocation.SHARED_CABIN,
            SeatLocation.TORPEDO_STORAGE_COMPARTMENT
        )
    );
}