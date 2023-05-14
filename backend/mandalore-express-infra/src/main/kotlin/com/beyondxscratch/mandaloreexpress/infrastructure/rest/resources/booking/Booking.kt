package com.beyondxscratch.mandaloreexpress.infrastructure.rest.resources.booking

import com.beyondxscratch.mandaloreexpress.infrastructure.rest.resources.Resource
import com.beyondxscratch.mandaloreexpress.infrastructure.rest.resources.sharedkernel.Price
import org.springframework.hateoas.RepresentationModel
import java.util.UUID

@Resource
class Booking(private val id: UUID, val spaceTrains: List<SpaceTrain>, val totalPrice: Price) : RepresentationModel<Booking>()