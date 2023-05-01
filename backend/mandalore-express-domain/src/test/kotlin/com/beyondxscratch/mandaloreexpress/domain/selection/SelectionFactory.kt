package com.beyondxscratch.mandaloreexpress.domain.selection

import com.beyondxscratch.mandaloreexpress.domain.Bound.OUTBOUND
import com.beyondxscratch.mandaloreexpress.domain.Bound.values

fun randomSelection() = Selection(mapOf(values().random() to randomSelectedSpaceTrain()))
fun selection() = Selection(mapOf(OUTBOUND to selectedSpaceTrain()))
