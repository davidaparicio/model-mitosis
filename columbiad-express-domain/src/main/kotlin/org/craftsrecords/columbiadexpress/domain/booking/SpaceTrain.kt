package org.craftsrecords.columbiadexpress.domain.booking

import org.craftsrecords.columbiadexpress.domain.sharedkernel.Fare
import org.craftsrecords.columbiadexpress.domain.sharedkernel.Schedule
import org.craftsrecords.columbiadexpress.domain.spaceport.SpacePort

data class SpaceTrain(val number: String,
                      val origin: SpacePort,
                      val destination: SpacePort,
                      val schedule: Schedule,
                      val fare: Fare)