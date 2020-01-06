package org.craftsrecords.columbiadexpress.infrastructure.rest.resources

import org.springframework.hateoas.RepresentationModel
import java.util.UUID
import org.craftsrecords.columbiadexpress.domain.search.Search as DomainSearch


@Resource
class Search(private val id: UUID, val criteria: Criteria, val spaceTrains: SpaceTrains) : RepresentationModel<Search>()

fun DomainSearch.toResource(): Search = Search(id, criteria.toResource(), spaceTrains.toResource())
