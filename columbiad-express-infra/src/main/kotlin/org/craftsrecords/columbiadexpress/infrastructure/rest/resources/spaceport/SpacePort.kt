package org.craftsrecords.columbiadexpress.infrastructure.rest.resources.spaceport

import org.craftsrecords.columbiadexpress.domain.spaceport.AstronomicalBody
import org.craftsrecords.columbiadexpress.infrastructure.rest.controllers.SpacePortsController
import org.craftsrecords.columbiadexpress.infrastructure.rest.resources.Resource
import org.springframework.hateoas.IanaLinkRelations.SELF
import org.springframework.hateoas.RepresentationModel
import org.springframework.hateoas.server.mvc.add
import org.craftsrecords.columbiadexpress.domain.spaceport.SpacePort as DomainSpacePort

@Resource
data class SpacePort(private val id: String, val name: String, val location: AstronomicalBody) : RepresentationModel<SpacePort>()

fun DomainSpacePort.toResource(): SpacePort {
    return SpacePort(id, name, location)
            .add(SpacePortsController::class) {
                linkTo { getSpacePortIdentifiedBy(id) } withRel SELF
            }
}