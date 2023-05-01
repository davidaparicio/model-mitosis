package com.beyondxscratch.mandaloreexpress.infrastructure.rest.controllers

import com.beyondxscratch.mandaloreexpress.domain.api.SearchForSpaceTrains
import com.beyondxscratch.mandaloreexpress.domain.spacetrain.Bound
import com.beyondxscratch.mandaloreexpress.domain.spacetrain.SpaceTrain.Companion.get
import com.beyondxscratch.mandaloreexpress.domain.spi.Searches
import com.beyondxscratch.mandaloreexpress.infrastructure.rest.resources.search.*
import com.beyondxscratch.mandaloreexpress.infrastructure.rest.resources.search.Search
import org.springframework.hateoas.IanaLinkRelations.SELF
import org.springframework.hateoas.LinkRelation
import org.springframework.hateoas.LinkRelation.of
import org.springframework.hateoas.RepresentationModel
import org.springframework.hateoas.server.EntityLinks
import org.springframework.hateoas.server.ExposesResourceFor
import org.springframework.hateoas.server.mvc.add
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.created
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDateTime.parse
import java.util.Locale
import java.util.UUID
import com.beyondxscratch.mandaloreexpress.domain.Search as DomainSearch
import com.beyondxscratch.mandaloreexpress.domain.criteria.Criteria as DomainCriteria
import com.beyondxscratch.mandaloreexpress.domain.criteria.Journey as DomainJourney
import com.beyondxscratch.mandaloreexpress.domain.spacetrain.SpaceTrain as DomainSpaceTrain
import com.beyondxscratch.mandaloreexpress.domain.spacetrain.SpaceTrains as DomainSpaceTrains


@RestController
@RequestMapping("/searches")
@ExposesResourceFor(Search::class)
class SearchController(
    private val `search for space trains`: SearchForSpaceTrains,
    private val searches: Searches,
    private val entityLinks: EntityLinks
) {

    @PostMapping
    fun performASearch(@RequestBody criteria: Criteria): ResponseEntity<Search> {
        try {
            val domainCriteria = criteria.toDomainObject()
            val domainSearch = `search for space trains` satisfying domainCriteria
            val search = domainSearch.toResource()
            return created(search.getRequiredLink(SELF).toUri()).body(search)
        } catch (ex: IllegalArgumentException) {
            throw ResponseStatusException(BAD_REQUEST, ex.message, ex)
        }

    }

    @GetMapping("/{searchId}")
    fun retrieveAnExistingSearch(@PathVariable searchId: UUID): ResponseEntity<Search> {
        val domainSearch = retrieveSearch(searchId)
        return ok(domainSearch.toResource())
    }


    @GetMapping("/{searchId}/spacetrains")
    fun retrieveSpaceTrainsForBound(
        @PathVariable searchId: UUID,
        @RequestParam bound: Bound
    ): ResponseEntity<SpaceTrains> {
        val domainSearch = retrieveSearch(searchId)
        val searchLink = searchLink(searchId)
        val spaceTrain = domainSearch.spaceTrains[bound]

        val spaceTrains = SpaceTrains(spaceTrain.toResource())
        spaceTrains
            .add(searchLink.withRel("search"))
            .linkToSpaceTrainsForBound(searchId, bound, SELF)
        return ok(spaceTrains)
    }

    private fun <R : RepresentationModel<R>> R.linkToSpaceTrainsForBound(
        searchId: UUID,
        bound: Bound,
        linkRelation: LinkRelation,
    ): R {
        return this.add(SearchController::class) {
            linkTo { retrieveSpaceTrainsForBound(searchId, bound) } withRel linkRelation
        }
    }

    private fun retrieveSearch(searchId: UUID) = (searches `find search identified by` searchId
        ?: throw ResponseStatusException(NOT_FOUND, "unknown search id $searchId"))

    private fun DomainSearch.toResource(): Search {
        val searchLink = searchLink(id)
        return Search(id, criteria.toResource())
            .add(searchLink.withSelfRel())
            .also { search ->
                spaceTrains.map { it.bound }.distinct()
                    .forEach { bound ->
                        search.linkToSpaceTrainsForBound(
                            id, bound, of(
                                "all-${
                                    bound.toString()
                                        .lowercase(Locale.getDefault())
                                }s"
                            )
                        )
                    }
            }
    }

    private fun searchLink(searchId: UUID) =
        entityLinks.linkForItemResource(Search::class.java, searchId)

    private fun Criteria.toDomainObject(): DomainCriteria =
        DomainCriteria(journeys.map {
            DomainJourney(
                it.departureSpacePortId.toString(),
                parse(it.departureSchedule),
                it.arrivalSpacePortId.toString()
            )
        })

    private fun DomainSpaceTrain.toResource(): SpaceTrain = SpaceTrain(
        number,
        bound,
        originId,
        destinationId,
        schedule.departure,
        schedule.arrival,
        schedule.duration
    )

    private fun DomainSpaceTrains.toResource(): List<SpaceTrain> = this.map { it.toResource() }
}





