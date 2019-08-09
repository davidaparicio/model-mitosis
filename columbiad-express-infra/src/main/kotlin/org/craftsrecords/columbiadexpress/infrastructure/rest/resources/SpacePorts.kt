package org.craftsrecords.columbiadexpress.infrastructure.rest.resources

import org.craftsrecords.columbiadexpress.infrastructure.rest.controllers.SpacePortsController
import org.springframework.hateoas.CollectionModel
import org.springframework.hateoas.IanaLinkRelations
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder
import org.springframework.hateoas.server.mvc.add
import org.craftsrecords.columbiadexpress.domain.spaceport.SpacePort as DomainSpacePort

open class SpacePorts(spaceports: Set<SpacePort>) : CollectionModel<SpacePort>(spaceports) {
    companion object {
        const val REL_ALL_SPACEPORTS: String = "allSpacePorts"
    }

    fun addLinks(partialNameRequest: String?): SpacePorts {
        add(SpacePortsController::class) {
            partialNameRequest?.let { linkTo { getSpacePorts(it) } withRel IanaLinkRelations.SELF }
        }
        add(WebMvcLinkBuilder.linkTo(SpacePortsController::class.java).withRel(REL_ALL_SPACEPORTS))
        addIf(partialNameRequest == null) { WebMvcLinkBuilder.linkTo(SpacePortsController::class.java).withSelfRel() }
        return this
    }
}

fun Set<DomainSpacePort>.toResource(): SpacePorts = SpacePorts(map(DomainSpacePort::toResource).toSet())