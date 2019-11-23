package org.craftsrecords.columbiadexpress.domain.search

import org.craftsrecords.columbiadexpress.domain.search.criteria.criteria
import java.util.UUID

fun search(): Search = Search(UUID.fromString("fa9f2371-5b13-40a1-bd18-42db3371f073"), criteria(), listOf(randomSpaceTrain()))
fun randomSearch(): Search = search().copy(id = UUID.randomUUID())