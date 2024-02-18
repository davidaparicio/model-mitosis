package com.beyondxscratch.mandaloreexpress.domain.search.selection

import com.beyondxscratch.mandaloreexpress.domain.search.spacetrain.price
import com.beyondxscratch.mandaloreexpress.domain.search.spacetrain.randomPrice
import java.util.UUID.nameUUIDFromBytes
import java.util.UUID.randomUUID

fun randomSelectedSpaceTrain() = SelectedSpaceTrain(randomUUID().toString(), randomUUID(), randomPrice())
fun selectedSpaceTrain() = SelectedSpaceTrain("MOON1", nameUUIDFromBytes("MOON1".toByteArray()), price())