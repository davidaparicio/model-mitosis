package com.beyondxscratch.mandaloreexpress.infrastructure.rest.resources.search

import com.beyondxscratch.mandaloreexpress.infrastructure.rest.resources.Resource
import org.springframework.hateoas.RepresentationModel
import java.util.UUID


@Resource
data class Search(private val id: UUID,
                  val criteria: Criteria) : RepresentationModel<Search>()
