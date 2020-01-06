package org.craftsrecords.columbiadexpress.infrastructure.rest.controllers

import org.craftsrecords.columbiadexpress.domain.spaceport.SpacePort
import org.craftsrecords.columbiadexpress.domain.spaceport.api.RetrieveSpacePorts
import org.craftsrecords.columbiadexpress.domain.spaceport.api.SearchForSpaceTrains
import org.craftsrecords.columbiadexpress.infrastructure.rest.resources.Criteria
import org.craftsrecords.columbiadexpress.infrastructure.rest.resources.Search
import org.craftsrecords.columbiadexpress.infrastructure.rest.resources.toResource
import org.springframework.hateoas.server.EntityLinks
import org.springframework.hateoas.server.ExposesResourceFor
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URI
import java.time.LocalDateTime.parse
import org.craftsrecords.columbiadexpress.domain.search.Search as DomainSearch
import org.craftsrecords.columbiadexpress.domain.search.criteria.Criteria as DomainCriteria
import org.craftsrecords.columbiadexpress.domain.search.criteria.Journey as DomainJourney


@RestController
@RequestMapping("/searches")
@ExposesResourceFor(Search::class)
class SearchController(private val `search for space trains`: SearchForSpaceTrains,
                       private val `retrieve space ports`: RetrieveSpacePorts,
                       private val entityLinks: EntityLinks) {

    @PostMapping
    fun performASearch(@RequestBody criteria: Criteria): ResponseEntity<Search> {
        val domainCriteria = criteria.toDomainObject()

        val domainSearch = `search for space trains` satisfying domainCriteria

        return domainSearch.toResponseEntity()
    }

    private fun DomainSearch.toResponseEntity(): ResponseEntity<Search> {
        val search = this.toResource()
        val selfLink = entityLinks.linkForItemResource(Search::class.java, this.id).withSelfRel()
        search.add(selfLink)
        return ResponseEntity.created(selfLink.toUri()).body(search)
    }

    private fun Criteria.toDomainObject(): DomainCriteria =
            DomainCriteria(journeys.map {
                DomainJourney(it.departureSpacePort.toDomainSpacePort(),
                        parse(it.departureSchedule),
                        it.arrivalSpacePort.toDomainSpacePort())
            })


    private fun URI.toDomainSpacePort(): SpacePort {
        val spacePortsUri = linkTo(SpacePortsController::class.java).toUri()
        val id = spacePortsUri.relativize(this).toString()
        return `retrieve space ports` `identified by` id
    }
}





