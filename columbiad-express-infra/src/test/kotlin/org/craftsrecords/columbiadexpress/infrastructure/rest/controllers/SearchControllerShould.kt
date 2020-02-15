package org.craftsrecords.columbiadexpress.infrastructure.rest.controllers

import com.jayway.jsonpath.JsonPath
import org.craftsrecords.columbiadexpress.domain.search.Bound.INBOUND
import org.craftsrecords.columbiadexpress.domain.search.Bound.OUTBOUND
import org.craftsrecords.columbiadexpress.domain.search.OneWay
import org.craftsrecords.columbiadexpress.domain.search.RoundTrip
import org.craftsrecords.columbiadexpress.domain.search.Search
import org.craftsrecords.columbiadexpress.domain.spi.Searches
import org.craftsrecords.columbiadexpress.infrastructure.configurations.DomainConfiguration
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.time.LocalDateTime.now
import java.time.temporal.ChronoUnit.MINUTES

@WebMvcTest(controllers = [SearchController::class])
@Import(DomainConfiguration::class)
class SearchControllerShould(@Autowired val mvc: MockMvc, @Autowired val searches: Searches) {

    private val departureSpacePort = "http://localhost/spaceports/ddf86b0b-94e3-3566-8486-fd076b9686a6"
    private val arrivalSpacePort = "http://localhost/spaceports/4ed3116c-e359-3245-b8d0-cec742551507"
    private val departureSchedule =
            now().plusDays(5)
                    .withHour(10)
                    .withMinute(0)
                    .truncatedTo(MINUTES)
    private val oneWayCriteria = """
{
	"journeys" : [{
			"departureSpacePort" : "$departureSpacePort",
			"departureSchedule" : "$departureSchedule",
			"arrivalSpacePort" : "$arrivalSpacePort"
        }]
}
"""

    private val roundTripCriteria = """
{
	"journeys" : [
                    {
			            "departureSpacePort" : "$departureSpacePort",
			            "departureSchedule" : "$departureSchedule",
			            "arrivalSpacePort" : "$arrivalSpacePort"
                    },
                    {
                    	"departureSpacePort" : "$arrivalSpacePort",
			            "departureSchedule" : "${departureSchedule.plusWeeks(3)}",
			            "arrivalSpacePort" : "$departureSpacePort"
                    }
                ]
}
"""

    @BeforeEach
    fun setUp(@OneWay oneWaySearch: Search, @RoundTrip roundTripSearch: Search) {
        searches.save(oneWaySearch)
        searches.save(roundTripSearch)
    }

    @Test
    fun `perform a search satisfying some one way criteria`() {
        lateinit var location: String
        mvc.perform(
                post("/searches")
                        .accept(APPLICATION_JSON_VALUE)
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(oneWayCriteria))
                //
                .andExpect(status().isCreated)
                .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.criteria.journeys").value(hasSize<Collection<*>>(1)))
                .andExpect(jsonPath("$.criteria.journeys[0].departureSpacePort").value(departureSpacePort))
                .andExpect(jsonPath("$.criteria.journeys[0].departureSchedule").value(departureSchedule.toString()))
                .andExpect(jsonPath("$.criteria.journeys[0].arrivalSpacePort").value(arrivalSpacePort))
                .andExpect(jsonPath("$.spaceTrains").doesNotHaveJsonPath())
                .andDo { location = it.response.getHeader("Location")!! }
                .andExpect(jsonPath("$._links.self.href").value(location))
                .andExpect(jsonPath("$._links.current-selection.href").value("$location/selection"))
                .andExpect(jsonPath("$._links.outbound-spacetrains.href").value("$location/spacetrains?bound=OUTBOUND"))
                .andExpect(jsonPath("$._links.inbound-spacetrains").doesNotHaveJsonPath())

        checkOutboundSpaceTrains(location)

        mvc.perform(
                get("$location/spacetrains?bound=INBOUND")
                        .accept(APPLICATION_JSON_VALUE))
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.spaceTrains").value(hasSize<Collection<*>>(0)))


    }

    private fun checkOutboundSpaceTrains(location: String) {
        lateinit var spaceTrainNumber: String
        mvc.perform(
                get("$location/spacetrains?bound=OUTBOUND")
                        .accept(APPLICATION_JSON_VALUE))
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.spaceTrains").value(hasSize<Collection<*>>(3)))
                .andExpect(jsonPath("$.spaceTrains[0].number").value(notNullValue()))
                .andDo { spaceTrainNumber = JsonPath.read(it.response.contentAsString, "$.spaceTrains[0].number") }
                .andExpect(jsonPath("$.spaceTrains[0].bound").value("OUTBOUND"))
                .andExpect(jsonPath("$.spaceTrains[0].origin.name").value("Centre Spatial Guyanais"))
                .andExpect(jsonPath("$.spaceTrains[0].origin.location").value("EARTH"))
                .andExpect(jsonPath("$.spaceTrains[0].origin._links.self.href").value(departureSpacePort))
                .andExpect(jsonPath("$.spaceTrains[0].number").value(notNullValue()))
                .andExpect(jsonPath("$.spaceTrains[0].destination.name").value("Mare Cognitum"))
                .andExpect(jsonPath("$.spaceTrains[0].destination.location").value("MOON"))
                .andExpect(jsonPath("$.spaceTrains[0].destination._links.self.href").value(arrivalSpacePort))
                .andExpect(jsonPath("$.spaceTrains[0].fares").value(hasSize<Collection<*>>(2)))
                .andExpect(jsonPath("$.spaceTrains[0].fares[0].comfortClass").value("FIRST"))
                .andExpect(jsonPath("$.spaceTrains[0].fares[0].price.amount").value(notNullValue()))
                .andExpect(jsonPath("$.spaceTrains[0].fares[0].price.currency").value("EUR"))
                .andExpect(jsonPath("$.spaceTrains[0].fares[0]._links.select.href").value(matchesPattern("$location/spacetrains/$spaceTrainNumber/fares/.*/select")))
                .andExpect(jsonPath("$._links.self.href").value("$location/spacetrains?bound=OUTBOUND"))
                .andExpect(jsonPath("$._links.search.href").value(location))
    }

    private fun checkInboundSpaceTrains(location: String) {
        lateinit var spaceTrainNumber: String
        mvc.perform(
                get("$location/spacetrains?bound=INBOUND")
                        .accept(APPLICATION_JSON_VALUE))
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.spaceTrains").value(hasSize<Collection<*>>(3)))
                .andExpect(jsonPath("$.spaceTrains[0].number").value(notNullValue()))
                .andDo { spaceTrainNumber = JsonPath.read(it.response.contentAsString, "$.spaceTrains[0].number") }
                .andExpect(jsonPath("$.spaceTrains[0].bound").value("INBOUND"))
                .andExpect(jsonPath("$.spaceTrains[0].origin.name").value("Mare Cognitum"))
                .andExpect(jsonPath("$.spaceTrains[0].origin.location").value("MOON"))
                .andExpect(jsonPath("$.spaceTrains[0].origin._links.self.href").value(arrivalSpacePort))
                .andExpect(jsonPath("$.spaceTrains[0].number").value(notNullValue()))
                .andExpect(jsonPath("$.spaceTrains[0].destination.name").value("Centre Spatial Guyanais"))
                .andExpect(jsonPath("$.spaceTrains[0].destination.location").value("EARTH"))
                .andExpect(jsonPath("$.spaceTrains[0].destination._links.self.href").value(departureSpacePort))
                .andExpect(jsonPath("$.spaceTrains[0].fares").value(hasSize<Collection<*>>(2)))
                .andExpect(jsonPath("$.spaceTrains[0].fares[0].comfortClass").value("FIRST"))
                .andExpect(jsonPath("$.spaceTrains[0].fares[0].price.amount").value(notNullValue()))
                .andExpect(jsonPath("$.spaceTrains[0].fares[0].price.currency").value("EUR"))
                .andExpect(jsonPath("$.spaceTrains[0].fares[0]._links.select.href").value(matchesPattern("$location/spacetrains/$spaceTrainNumber/fares/.*/select")))
                .andExpect(jsonPath("$._links.self.href").value("$location/spacetrains?bound=INBOUND"))
                .andExpect(jsonPath("$._links.search.href").value(location))
    }

    @Test
    fun `perform a search satisfying some roundtrip criteria`() {
        lateinit var location: String
        mvc.perform(
                post("/searches")
                        .accept(APPLICATION_JSON_VALUE)
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(roundTripCriteria))
                //
                .andExpect(status().isCreated)
                .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.criteria.journeys").value(hasSize<Collection<*>>(2)))
                .andExpect(jsonPath("$.criteria.journeys[0].departureSpacePort").value(departureSpacePort))
                .andExpect(jsonPath("$.criteria.journeys[0].departureSchedule").value(departureSchedule.toString()))
                .andExpect(jsonPath("$.criteria.journeys[0].arrivalSpacePort").value(arrivalSpacePort))
                .andExpect(jsonPath("$.criteria.journeys[1].departureSpacePort").value(arrivalSpacePort))
                .andExpect(jsonPath("$.criteria.journeys[1].departureSchedule").value(departureSchedule.plusWeeks(3).toString()))
                .andExpect(jsonPath("$.criteria.journeys[1].arrivalSpacePort").value(departureSpacePort))
                .andExpect(jsonPath("$.spaceTrains").doesNotHaveJsonPath())
                .andDo { location = it.response.getHeader("Location")!! }
                .andExpect(jsonPath("$._links.self.href").value(location))
                .andExpect(jsonPath("$._links.current-selection.href").value("$location/selection"))
                .andExpect(jsonPath("$._links.outbound-spacetrains.href").value("$location/spacetrains?bound=OUTBOUND"))
                .andExpect(jsonPath("$._links.inbound-spacetrains.href").value("$location/spacetrains?bound=INBOUND"))

        checkOutboundSpaceTrains(location)
        checkInboundSpaceTrains(location)

        mvc.perform(
                get("$location/selection")
                        .accept(APPLICATION_JSON_VALUE))
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.spaceTrains").value(hasSize<Collection<*>>(0)))
                .andExpect(jsonPath("$.totalPrice").doesNotHaveJsonPath())
                .andExpect(jsonPath("$._links.search.href").value(location))
                .andExpect(jsonPath("$._links.self.href").value("$location/selection"))

    }

    @Test
    fun `retrieve an existing search`(@RoundTrip search: Search) {
        mvc.perform(
                get("/searches/${search.id}")
                        .accept(APPLICATION_JSON_VALUE))
                //
                .andExpect(status().isOk)
                .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$._links.self.href").value("http://localhost/searches/${search.id}"))
    }

    @Test
    fun `return 404 when search id is unknown`() {
        mvc.perform(
                get("/searches/8ca7339a-4fc7-4b08-ad49-593d3ff12e03")
                        .accept(APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound)
    }

    @Test
    fun `select space trains with fares`(@RoundTrip search: Search) {
        val outBoundSpaceTrain = search.spaceTrains.first { it.bound == OUTBOUND }
        val outBoundFare = outBoundSpaceTrain.fares.first()
        val inBoundSpaceTrain = search.spaceTrains.first { it.bound == INBOUND }
        val inBoundFare = inBoundSpaceTrain.fares.first()
        val location = "http://localhost/searches/${search.id}"

        val selectedSpaceTrainsAndFares = arrayListOf(outBoundSpaceTrain to outBoundFare, inBoundSpaceTrain to inBoundFare)

        mvc.perform(
                post("/searches/${search.id}/spacetrains/${inBoundSpaceTrain.number}/fares/${inBoundFare.id}/select")
                        .accept(APPLICATION_JSON_VALUE))

        mvc.perform(
                post("/searches/${search.id}/spacetrains/${outBoundSpaceTrain.number}/fares/${outBoundFare.id}/select")
                        .accept(APPLICATION_JSON_VALUE))
                //
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.spaceTrains").value(hasSize<Collection<*>>(2)))
                .also { result ->
                    selectedSpaceTrainsAndFares
                            .forEachIndexed { index, spaceTrainFare ->
                                val (spaceTrain, fare) = spaceTrainFare
                                result.andExpect(jsonPath("$.spaceTrains[$index].bound").value(spaceTrain.bound.toString()))
                                        .andExpect(jsonPath("$.spaceTrains[$index].number").value(spaceTrain.number))
                                        .andExpect(jsonPath("$.spaceTrains[$index].origin.name").value(spaceTrain.origin.name))
                                        .andExpect(jsonPath("$.spaceTrains[$index].origin.location").value(spaceTrain.origin.location.toString()))
                                        .andExpect(jsonPath("$.spaceTrains[$index].origin._links.self.href").value("http://localhost/spaceports/${spaceTrain.origin.id}"))
                                        .andExpect(jsonPath("$.spaceTrains[$index].destination.name").value(spaceTrain.destination.name))
                                        .andExpect(jsonPath("$.spaceTrains[$index].destination.location").value(spaceTrain.destination.location.toString()))
                                        .andExpect(jsonPath("$.spaceTrains[$index].destination._links.self.href").value("http://localhost/spaceports/${spaceTrain.destination.id}"))
                                        .andExpect(jsonPath("$.spaceTrains[$index].fare.comfortClass").value(fare.comfortClass.toString()))
                                        .andExpect(jsonPath("$.spaceTrains[$index].fare.price.amount").value(fare.price.amount))
                                        .andExpect(jsonPath("$.spaceTrains[$index].fare.price.currency").value(fare.price.currency.toString()))

                            }
                }

                .andExpect(jsonPath("$.totalPrice.amount").value(outBoundFare.price.amount + inBoundFare.price.amount))
                .andExpect(jsonPath("$.totalPrice.currency").value(outBoundFare.price.currency.toString()))
                .andExpect(jsonPath("$._links.search.href").value(location))
                .andExpect(jsonPath("$._links.self.href").value("$location/selection"))
                .andExpect(jsonPath("$._links.outbound-spacetrains.href").value("$location/spacetrains?bound=OUTBOUND"))
                .andExpect(jsonPath("$._links.inbound-spacetrains.href").value("$location/spacetrains?bound=INBOUND"))
    }
}