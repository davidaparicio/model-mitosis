package org.craftsrecords.columbiadexpress.infrastructure.rest.resources

import org.craftsrecords.columbiadexpress.domain.sharedkernel.Price
import org.springframework.hateoas.RepresentationModel


@Resource
data class Selection(val spaceTrains: List<SelectedSpaceTrain>, val totalPrice: Price?) : RepresentationModel<Selection>()