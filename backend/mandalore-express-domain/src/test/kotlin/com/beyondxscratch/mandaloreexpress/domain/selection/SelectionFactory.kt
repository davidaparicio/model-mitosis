package com.beyondxscratch.mandaloreexpress.domain.selection

import com.beyondxscratch.mandaloreexpress.domain.search.selection.Selection
import com.beyondxscratch.mandaloreexpress.domain.search.spacetrain.Bound.OUTBOUND
import com.beyondxscratch.mandaloreexpress.domain.search.spacetrain.Bound.values

fun randomSelection() = Selection(mapOf(values().random() to randomSelectedSpaceTrain()))
fun selection() = Selection(mapOf(OUTBOUND to selectedSpaceTrain()))
