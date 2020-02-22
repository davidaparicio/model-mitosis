package org.craftsrecords.columbiadexpress.infrastructure.rest.resources.search

import org.craftsrecords.columbiadexpress.domain.sharedkernel.Price
import org.craftsrecords.columbiadexpress.infrastructure.rest.resources.Resource
import org.springframework.hateoas.RepresentationModel


@Resource
data class Selection(val spaceTrains: List<SelectedSpaceTrain>, val totalPrice: Price?) : RepresentationModel<Selection>()