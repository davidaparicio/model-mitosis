package com.beyondxscratch.mandaloreexpress.domain.selection

import com.beyondxscratch.mandaloreexpress.domain.search.selection.SelectedSpaceTrain
import com.beyondxscratch.mandaloreexpress.domain.spacetrain.fare.price
import com.beyondxscratch.mandaloreexpress.domain.spacetrain.fare.randomPrice
import java.util.UUID.nameUUIDFromBytes
import java.util.UUID.randomUUID

fun randomSelectedSpaceTrain() = SelectedSpaceTrain(randomUUID().toString(), randomUUID(), randomPrice())
fun selectedSpaceTrain() = SelectedSpaceTrain("MOON1", nameUUIDFromBytes("MOON1".toByteArray()), price())