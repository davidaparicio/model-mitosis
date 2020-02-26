package org.craftsrecords.columbiadexpress.infrastructure.rest.controllers

import org.craftsrecords.columbiadexpress.domain.api.RetrieveSpacePorts
import org.craftsrecords.columbiadexpress.domain.api.SearchForSpaceTrains
import org.craftsrecords.columbiadexpress.domain.api.SelectSpaceTrain
import org.craftsrecords.columbiadexpress.domain.api.`by resetting the selection`
import org.craftsrecords.columbiadexpress.domain.api.`in search`
import org.craftsrecords.columbiadexpress.domain.api.`with the fare`
import org.craftsrecords.columbiadexpress.domain.search.SpaceTrain.Companion.get
import org.craftsrecords.columbiadexpress.domain.sharedkernel.Bound
import org.craftsrecords.columbiadexpress.domain.spaceport.SpacePort
import org.craftsrecords.columbiadexpress.domain.spi.Searches
import org.craftsrecords.columbiadexpress.infrastructure.rest.resources.Fare
import org.craftsrecords.columbiadexpress.infrastructure.rest.resources.Fares
import org.craftsrecords.columbiadexpress.infrastructure.rest.resources.search.Criteria
import org.craftsrecords.columbiadexpress.infrastructure.rest.resources.search.Search
import org.craftsrecords.columbiadexpress.infrastructure.rest.resources.search.SelectedSpaceTrain
import org.craftsrecords.columbiadexpress.infrastructure.rest.resources.search.Selection
import org.craftsrecords.columbiadexpress.infrastructure.rest.resources.search.SpaceTrain
import org.craftsrecords.columbiadexpress.infrastructure.rest.resources.search.SpaceTrains
import org.craftsrecords.columbiadexpress.infrastructure.rest.resources.search.toResource
import org.craftsrecords.columbiadexpress.infrastructure.rest.resources.spaceport.toResource
import org.springframework.hateoas.IanaLinkRelations.SELF
import org.springframework.hateoas.LinkRelation
import org.springframework.hateoas.LinkRelation.of
import org.springframework.hateoas.RepresentationModel
import org.springframework.hateoas.server.EntityLinks
import org.springframework.hateoas.server.ExposesResourceFor
import org.springframework.hateoas.server.LinkBuilder
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn
import org.springframework.hateoas.server.mvc.add
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
import java.net.URI
import java.time.LocalDateTime.parse
import java.util.UUID
import org.craftsrecords.columbiadexpress.domain.search.Search as DomainSearch
import org.craftsrecords.columbiadexpress.domain.search.SpaceTrain as DomainSpaceTrain
import org.craftsrecords.columbiadexpress.domain.search.SpaceTrains as DomainSpaceTrains
import org.craftsrecords.columbiadexpress.domain.search.criteria.Criteria as DomainCriteria
import org.craftsrecords.columbiadexpress.domain.search.criteria.Journey as DomainJourney
import org.craftsrecords.columbiadexpress.domain.sharedkernel.Fare as DomainFare
import org.craftsrecords.columbiadexpress.domain.sharedkernel.Fares as DomainFares

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
    fun retrieveSpaceTrainsForBound(@PathVariable searchId: UUID, @RequestParam bound: Bound, @RequestParam onlySelectable: Boolean = false): ResponseEntity<SpaceTrains> {
        val domainSearch = retrieveSearch(searchId)
        val searchLink = searchLink(searchId)
        val spaceTrain =
                when {
                    onlySelectable -> domainSearch.selectableSpaceTrains(bound)
                    else -> domainSearch.spaceTrains[bound]
                }

        val spaceTrains = SpaceTrains(spaceTrain.toResource(searchLink, !onlySelectable))
        spaceTrains
                .add(searchLink.withRel("search"))
                .add(searchLink.slash("selection").withRel("selection"))
                .linkToSpaceTrainsForBound(searchId, bound, SELF, onlySelectable)
        return ok(spaceTrains)
    }

    @PostMapping("/{searchId}/spacetrains/{spaceTrainNumber}/fares/{fareId}/select")
    fun selectSpaceTrainWithFare(@PathVariable searchId: UUID, @PathVariable spaceTrainNumber: String, @PathVariable fareId: UUID, @RequestParam resetSelection: Boolean = false): ResponseEntity<Selection> {
        val search =
                `select space train` `having the number` spaceTrainNumber `with the fare` fareId `in search` searchId `by resetting the selection` resetSelection
        val selection = search.toSelectionResource()
        return ok(selection)
    }

    @GetMapping("/{searchId}/selection")
    fun selectSpaceTrainWithFare(@PathVariable searchId: UUID): ResponseEntity<Selection> {
        val search = retrieveSearch(searchId)
        val selection = search.toSelectionResource()
        return ok(selection)
    }

    private fun <R : RepresentationModel<R>> R.linkToSpaceTrainsForBound(searchId: UUID, bound: Bound, linkRelation: LinkRelation, onlySelectable: Boolean = false): R {
        return this.add(SearchController::class) {
            linkTo { retrieveSpaceTrainsForBound(searchId, bound, onlySelectable) } withRel linkRelation
        }
    }

    private fun retrieveSearch(searchId: UUID) = (searches `find search identified by` searchId
            ?: throw ResponseStatusException(NOT_FOUND, "unknown search id $searchId"))

    private fun DomainSearch.toResource(): Search {
        val searchLink = searchLink(id)
        return Search(id, criteria.toResource())
                .add(searchLink.withSelfRel())
                .add(searchLink.slash("selection").withRel("current-selection"))
                .addIf(isSelectionComplete()) {
                    linkTo(methodOn(BookingController::class.java).bookSomeSpaceTrainsFromTheSelectionOf(id)).withRel("create-booking")
                }
                .also { search ->
                    spaceTrains.map { it.bound }.distinct()
                            .forEach { bound ->
                                search.linkToSpaceTrainsForBound(id, bound, of("all-${bound.toString().toLowerCase()}s"))
                                search.addIf(this.selection.hasASelectionFor(bound.oppositeWay())) {
                                    linkTo(methodOn(SearchController::class.java).retrieveSpaceTrainsForBound(id, bound, true)).withRel("${bound.name.toLowerCase()}s-for-current-selection")
                                }
                            }
                }
    }

    private fun DomainSearch.toSelectionResource(): Selection {
        val searchLink = searchLink(id)
        val selectedSpaceTrain =
                selection.spaceTrains
                        .map { selectedSpaceTrain ->
                            val spaceTrain = spaceTrains.first { it.number == selectedSpaceTrain.spaceTrainNumber }
                            SelectedSpaceTrain(spaceTrain.number, spaceTrain.bound, spaceTrain.origin.toResource(), spaceTrain.destination.toResource(), spaceTrain.schedule.departure, spaceTrain.schedule.arrival, spaceTrain.fares.first { it.id == selectedSpaceTrain.fareId }.toResource())
                        }
                        .sortedBy { it.bound.ordinal }

        return Selection(selectedSpaceTrain, selection.totalPrice)
                .add(searchLink.withRel("search"))
                .add(searchLink.slash("selection").withSelfRel())
                .addIf(isSelectionComplete()) {
                    linkTo(methodOn(BookingController::class.java).bookSomeSpaceTrainsFromTheSelectionOf(id)).withRel("create-booking")
                }
                .also { selection ->
                    spaceTrains.map { it.bound }.distinct()
                            .asSequence()
                            .forEach { bound ->
                                selection.linkToSpaceTrainsForBound(id, bound, of("all-${bound.name.toLowerCase()}s"))
                                selection.addIf(this.selection.hasASelectionFor(bound.oppositeWay())) {
                                    linkTo(methodOn(SearchController::class.java).retrieveSpaceTrainsForBound(id, bound, true)).withRel("${bound.name.toLowerCase()}s-for-current-selection")
                                }
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

    private fun DomainSpaceTrain.toResource(searchLink: LinkBuilder, resetSelection: Boolean): SpaceTrain = SpaceTrain(
            number,
            bound,
            origin.toResource(),
            destination.toResource(),
            schedule.departure,
            schedule.arrival,
            schedule.duration,
            fares.toResource(searchLink.slash("spacetrains").slash(number), resetSelection))

    private fun DomainSpaceTrains.toResource(searchLink: LinkBuilder, resetSelection: Boolean = false): List<SpaceTrain> = this.map { it.toResource(searchLink, resetSelection) }

    private fun DomainFares.toResource(spaceTrainLink: LinkBuilder, resetSelection: Boolean): Fares = this.map { it.toResource(spaceTrainLink, resetSelection) }.toSet()

    private fun DomainFare.toResource(spaceTrainLink: LinkBuilder? = null, resetSelection: Boolean = false): Fare {
        val fare = Fare(id, comfortClass, price)

        spaceTrainLink?.let {
            fare.add(spaceTrainLink.slash("fares")
                    .slash("$id")
                    .slash("select?resetSelection=$resetSelection")
                    .withRel("select"))
        }
        return fare
    }
}





