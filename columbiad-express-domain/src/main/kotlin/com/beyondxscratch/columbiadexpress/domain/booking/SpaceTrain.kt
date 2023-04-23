package com.beyondxscratch.columbiadexpress.domain.booking

import com.beyondxscratch.columbiadexpress.domain.sharedkernel.Fare
import com.beyondxscratch.columbiadexpress.domain.sharedkernel.Schedule

data class SpaceTrain(val number: String,
                      val originId: String,
                      val destinationId: String,
                      val schedule: Schedule,
                      val fare: Fare)