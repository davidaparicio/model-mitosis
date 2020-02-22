package org.craftsrecords.columbiadexpress.infrastructure.rest.resources.search

import org.craftsrecords.columbiadexpress.infrastructure.rest.resources.Resource
import org.springframework.hateoas.RepresentationModel
import java.util.UUID


@Resource
data class Search(private val id: UUID,
                  val criteria: Criteria) : RepresentationModel<Search>()
