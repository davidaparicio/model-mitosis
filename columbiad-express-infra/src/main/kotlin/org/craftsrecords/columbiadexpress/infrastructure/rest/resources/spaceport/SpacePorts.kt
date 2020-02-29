package org.craftsrecords.columbiadexpress.infrastructure.rest.resources.spaceport

import org.craftsrecords.columbiadexpress.infrastructure.rest.controllers.SpacePortsController
import org.craftsrecords.columbiadexpress.infrastructure.rest.resources.Resource
import org.springframework.hateoas.CollectionModel
import org.springframework.hateoas.IanaLinkRelations.SELF
import org.springframework.hateoas.server.mvc.add
import org.craftsrecords.columbiadexpress.domain.spaceport.SpacePort as DomainSpacePort

@Resource
class SpacePorts(spaceports: Set<SpacePort>) : CollectionModel<SpacePort>(spaceports) {
    companion object {
        const val SPACEPORTS: String = "spaceports"
    }

    fun addLinks(partialNameRequest: String?): SpacePorts {
        add(SpacePortsController::class) {
            partialNameRequest?.let {
                linkTo { getSpacePorts(it) } withRel SELF
                linkTo { getSpacePorts(null) } withRel SPACEPORTS
            } ?: linkTo { getSpacePorts(null) } withRel SELF

        }
        return this
    }
}

fun Set<DomainSpacePort>.toResource(): SpacePorts = SpacePorts(map(DomainSpacePort::toResource).toSet())