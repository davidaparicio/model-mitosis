package org.craftsrecords.columbiadexpress.infrastructure.rest.resources.booking

import org.craftsrecords.columbiadexpress.domain.sharedkernel.Price
import org.craftsrecords.columbiadexpress.infrastructure.rest.resources.Resource
import org.springframework.hateoas.RepresentationModel
import java.util.UUID

@Resource
class Booking(private val id: UUID, val spaceTrains: List<SpaceTrain>, val totalPrice: Price) : RepresentationModel<Booking>()