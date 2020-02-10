package org.craftsrecords.columbiadexpress.domain.search.selection

import org.craftsrecords.columbiadexpress.domain.search.Bound.OUTBOUND
import org.craftsrecords.columbiadexpress.domain.search.Bound.values

fun randomSelection() = Selection(mapOf(values().random() to randomSelectedSpaceTrain()))
fun selection() = Selection(mapOf(OUTBOUND to selectedSpaceTrain()))
