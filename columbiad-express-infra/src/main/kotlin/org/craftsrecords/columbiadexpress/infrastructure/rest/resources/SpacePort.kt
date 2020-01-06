package org.craftsrecords.columbiadexpress.infrastructure.rest.resources

import org.craftsrecords.columbiadexpress.domain.spaceport.AstronomicalBody
import org.craftsrecords.columbiadexpress.infrastructure.rest.controllers.SpacePortsController
import org.springframework.hateoas.IanaLinkRelations
import org.springframework.hateoas.RepresentationModel
import org.springframework.hateoas.server.mvc.add
import org.craftsrecords.columbiadexpress.domain.spaceport.SpacePort as DomainSpacePort

@Resource
class SpacePort(private val id: String, val name: String, val location: AstronomicalBody) : RepresentationModel<SpacePort>()

fun DomainSpacePort.toResource(): SpacePort {
    return SpacePort(id, name, location)
            .add(SpacePortsController::class) {
                linkTo { getSpacePortIdentifiedBy(id) } withRel IanaLinkRelations.SELF
            }
}