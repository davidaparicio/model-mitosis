package org.craftsrecords.columbiadexpress.infrastructure.rest.resources

import org.springframework.hateoas.RepresentationModel
import java.util.*


@Resource
data class Search(private val id: UUID,
                  val criteria: Criteria) : RepresentationModel<Search>()
