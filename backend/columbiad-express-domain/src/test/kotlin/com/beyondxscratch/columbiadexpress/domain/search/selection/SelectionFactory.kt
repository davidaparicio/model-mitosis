package com.beyondxscratch.columbiadexpress.domain.search.selection

import com.beyondxscratch.columbiadexpress.domain.sharedkernel.Bound.OUTBOUND
import com.beyondxscratch.columbiadexpress.domain.sharedkernel.Bound.values

fun randomSelection() = Selection(mapOf(values().random() to randomSelectedSpaceTrain()))
fun selection() = Selection(mapOf(OUTBOUND to selectedSpaceTrain()))
