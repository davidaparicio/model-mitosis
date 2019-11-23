package org.craftsrecords.columbiadexpress.domain.search

import org.craftsrecords.columbiadexpress.domain.search.criteria.Criteria
import java.util.UUID
import java.util.UUID.randomUUID

data class Search(val id: UUID = randomUUID(), val criteria: Criteria, val spaceTrains: List<SpaceTrain>)