package org.craftsrecords.columbiadexpress.domain.booking

import org.craftsrecords.columbiadexpress.domain.sharedkernel.Fare
import org.craftsrecords.columbiadexpress.domain.sharedkernel.Schedule

data class SpaceTrain(val number: String,
                      val originId: String,
                      val destinationId: String,
                      val schedule: Schedule,
                      val fare: Fare)