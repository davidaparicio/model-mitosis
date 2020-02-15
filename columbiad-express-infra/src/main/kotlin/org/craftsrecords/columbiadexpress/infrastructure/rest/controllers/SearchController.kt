package org.craftsrecords.columbiadexpress.infrastructure.rest.controllers

import org.craftsrecords.columbiadexpress.domain.api.*
import org.craftsrecords.columbiadexpress.domain.search.Bound
import org.craftsrecords.columbiadexpress.domain.spaceport.SpacePort
import org.craftsrecords.columbiadexpress.domain.spi.Searches
import org.craftsrecords.columbiadexpress.infrastructure.rest.resources.*
import org.craftsrecords.columbiadexpress.infrastructure.rest.resources.Fare
import org.craftsrecords.columbiadexpress.infrastructure.rest.resources.Search
import org.springframework.hateoas.IanaLinkRelations.SELF
import org.springframework.hateoas.LinkRelation
import org.springframework.hateoas.LinkRelation.of
import org.springframework.hateoas.RepresentationModel
import org.springframework.hateoas.server.EntityLinks
import org.springframework.hateoas.server.ExposesResourceFor
import org.springframework.hateoas.server.LinkBuilder
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.hateoas.server.mvc.add
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.created
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.net.URI
import java.time.LocalDateTime.parse
import java.util.*
import org.craftsrecords.columbiadexpress.domain.search.Fare as DomainFare
import org.craftsrecords.columbiadexpress.domain.search.Fares as DomainFares
import org.craftsrecords.columbiadexpress.domain.search.Search as DomainSearch
import org.craftsrecords.columbiadexpress.domain.search.SpaceTrain as DomainSpaceTrain
import org.craftsrecords.columbiadexpress.domain.search.SpaceTrains as DomainSpaceTrains
import org.craftsrecords.columbiadexpress.domain.search.criteria.Criteria as DomainCriteria
import org.craftsrecords.columbiadexpress.domain.search.criteria.Journey as DomainJourney

@RestController
@RequestMapping("/searches")
@ExposesResourceFor(Search::class)
class SearchController(private val `search for space trains`: SearchForSpaceTrains,
                       private val `retrieve space ports`: RetrieveSpacePorts,
                       private val `select space train`: SelectSpaceTrain,
                       private val searches: Searches,
                       private val entityLinks: EntityLinks) {

    @PostMapping
    fun performASearch(@RequestBody criteria: Criteria): ResponseEntity<Search> {
        val domainCriteria = criteria.toDomainObject()

        val domainSearch = `search for space trains` satisfying domainCriteria
        val search = domainSearch.toResource()

        return created(search.getRequiredLink(SELF).toUri()).body(search)
    }

    @GetMapping("/{searchId}")
    fun retrieveAnExistingSearch(@PathVariable searchId: UUID): ResponseEntity<Search> {
        val domainSearch = retrieveSearch(searchId)
        return ok(domainSearch.toResource())
    }


    @GetMapping("/{searchId}/spacetrains")
    fun retrieveSpaceTrainsForBound(@PathVariable searchId: UUID, @RequestParam bound: Bound): ResponseEntity<SpaceTrains> {
        val domainSearch = retrieveSearch(searchId)
        val searchLink = searchLink(searchId)
        val spaceTrains = SpaceTrains(domainSearch.spaceTrains.filter { it.bound == bound }.toResource(searchLink))
        spaceTrains
                .add(searchLink.withRel("search"))
                .linkToSpaceTrainsForBound(searchId, bound, SELF)
        return ok(spaceTrains)
    }

    @PostMapping("/{searchId}/spacetrains/{spaceTrainNumber}/fares/{fareId}/select")
    fun selectSpaceTrainWithFare(@PathVariable searchId: UUID, @PathVariable spaceTrainNumber: String, @PathVariable fareId: UUID): ResponseEntity<Selection> {
        val search =
                `select space train` `having the number` spaceTrainNumber `with the fare` fareId `in search` searchId
        val selection = search.toSelectionResource()
        return ok(selection)
    }

    @GetMapping("/{searchId}/selection")
    fun selectSpaceTrainWithFare(@PathVariable searchId: UUID): ResponseEntity<Selection> {
        val search = retrieveSearch(searchId)
        val selection = search.toSelectionResource()
        return ok(selection)
    }

    private fun <R : RepresentationModel<R>> R.linkToSpaceTrainsForBound(searchId: UUID, bound: Bound, linkRelation: LinkRelation): R {
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
                .add(searchLink.slash("selection").withRel("current-selection"))
                .also { search ->
                    spaceTrains.map { it.bound }.distinct()
                            .forEach { bound ->
                                search.linkToSpaceTrainsForBound(id, bound, of("${bound.toString().toLowerCase()}-spacetrains"))
                            }
                }
    }

    private fun DomainSearch.toSelectionResource(): Selection {
        val searchLink = searchLink(id)
        val selectedSpaceTrain =
                selection.selectedSpaceTrains.values
                        .map { selectedSpaceTrain ->
                            val spaceTrain = spaceTrains.first { it.number == selectedSpaceTrain.spaceTrainNumber }
                            SelectedSpaceTrain(spaceTrain.number, spaceTrain.bound, spaceTrain.origin.toResource(), spaceTrain.destination.toResource(), spaceTrain.departureSchedule, spaceTrain.arrivalSchedule, spaceTrain.fares.first { it.id == selectedSpaceTrain.fareId }.toResource())
                        }
                        .sortedBy { it.bound.ordinal }

        return Selection(selectedSpaceTrain, selection.totalPrice)
                .add(searchLink.withRel("search"))
                .add(searchLink.slash("selection").withSelfRel())
                .also { selection ->
                    spaceTrains.map { it.bound }.distinct()
                            .forEach { bound ->
                                selection.linkToSpaceTrainsForBound(id, bound, of("${bound.toString().toLowerCase()}-spacetrains"))
                            }
                }
    }

    private fun searchLink(searchId: UUID) =
            entityLinks.linkForItemResource(Search::class.java, searchId)

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

    private fun DomainSpaceTrain.toResource(searchLink: LinkBuilder): SpaceTrain = SpaceTrain(
            number,
            bound,
            origin.toResource(),
            destination.toResource(),
            departureSchedule,
            arrivalSchedule,
            fares.toResource(searchLink.slash("spacetrains").slash(number)))

    private fun DomainSpaceTrains.toResource(searchLink: LinkBuilder): List<SpaceTrain> = this.map { it.toResource(searchLink) }

    private fun DomainFares.toResource(spaceTrainLink: LinkBuilder): Fares = this.map { it.toResource(spaceTrainLink) }.toSet()

    private fun DomainFare.toResource(spaceTrainLink: LinkBuilder? = null): Fare {
        val fare = Fare(id, comfortClass, price)

        spaceTrainLink?.let {
            fare.add(spaceTrainLink.slash("fares")
                    .slash("$id")
                    .slash("select")
                    .withRel("select"))
        }
        return fare
    }
}





