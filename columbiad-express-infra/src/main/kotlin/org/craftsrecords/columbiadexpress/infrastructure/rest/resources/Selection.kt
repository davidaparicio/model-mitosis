package org.craftsrecords.columbiadexpress.infrastructure.rest.resources

import org.springframework.hateoas.RepresentationModel


@Resource
data class Selection(val spaceTrains: List<SelectedSpaceTrain>) : RepresentationModel<Selection>()