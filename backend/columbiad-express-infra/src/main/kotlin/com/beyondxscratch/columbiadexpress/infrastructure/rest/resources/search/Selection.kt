package com.beyondxscratch.columbiadexpress.infrastructure.rest.resources.search

import com.beyondxscratch.columbiadexpress.domain.sharedkernel.Price
import com.beyondxscratch.columbiadexpress.infrastructure.rest.resources.Resource
import org.springframework.hateoas.RepresentationModel


@Resource
data class Selection(val spaceTrains: List<SelectedSpaceTrain>, val totalPrice: Price?) : RepresentationModel<Selection>()