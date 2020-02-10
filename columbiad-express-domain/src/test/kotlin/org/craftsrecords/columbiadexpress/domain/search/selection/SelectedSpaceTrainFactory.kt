package org.craftsrecords.columbiadexpress.domain.search.selection

import java.util.UUID.nameUUIDFromBytes
import java.util.UUID.randomUUID

fun randomSelectedSpaceTrain() = SelectedSpaceTrain(randomUUID().toString(), randomUUID())
fun selectedSpaceTrain() = SelectedSpaceTrain("MOON1", nameUUIDFromBytes("MOON1".toByteArray()))