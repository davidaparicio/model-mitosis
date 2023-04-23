package com.beyondxscratch.columbiadexpress.domain.search.selection

import com.beyondxscratch.columbiadexpress.domain.sharedkernel.price
import com.beyondxscratch.columbiadexpress.domain.sharedkernel.randomPrice
import java.util.UUID.nameUUIDFromBytes
import java.util.UUID.randomUUID

fun randomSelectedSpaceTrain() = SelectedSpaceTrain(randomUUID().toString(), randomUUID(), randomPrice())
fun selectedSpaceTrain() = SelectedSpaceTrain("MOON1", nameUUIDFromBytes("MOON1".toByteArray()), price())