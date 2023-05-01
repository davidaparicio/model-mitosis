package com.beyondxscratch.mandaloreexpress.infrastructure.rest.controllers

import com.beyondxscratch.mandaloreexpress.domain.OneWay
import com.beyondxscratch.mandaloreexpress.domain.RoundTrip
import com.beyondxscratch.mandaloreexpress.domain.Search
import com.beyondxscratch.mandaloreexpress.domain.spaceport.OnCoruscant
import com.beyondxscratch.mandaloreexpress.domain.spaceport.OnMandalore
import com.beyondxscratch.mandaloreexpress.domain.spaceport.SpacePort
import com.beyondxscratch.mandaloreexpress.domain.spacetrain.Bound.OUTBOUND
import com.beyondxscratch.mandaloreexpress.domain.spacetrain.SpaceTrain.Companion.get
import com.beyondxscratch.mandaloreexpress.domain.spi.Searches
import com.beyondxscratch.mandaloreexpress.infrastructure.configurations.DomainConfiguration
import com.jayway.jsonpath.JsonPath
import org.hamcrest.Matchers.hasSize
import org.hamcrest.Matchers.notNullValue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.hateoas.MediaTypes.HAL_JSON
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDateTime.now
import java.time.temporal.ChronoUnit.MINUTES

@WebMvcTest(controllers = [SearchController::class])
@Import(DomainConfiguration::class)
class SearchControllerShould(
    @Autowired val mvc: MockMvc,
    @Autowired val searches: Searches,
    @OnCoruscant onCoruscantSpacePort: SpacePort,
    @OnMandalore onMandaloreSpacePort: SpacePort
) {

    private val departureSpacePortId = "http://localhost:1865/spaceports/${onCoruscantSpacePort.id}"
    private val arrivalSpacePortId = "http://localhost:1865/spaceports/${onMandaloreSpacePort.id}"
    private val outboundDepartureSchedule =
        now().plusDays(5)
            .withHour(10)
            .withMinute(0)
            .truncatedTo(MINUTES)

    private val inboundDepartureSchedule = outboundDepartureSchedule.plusWeeks(3)

    private val oneWayCriteria = """
{
	"journeys" : [{
			"departureSpacePortId" : "$departureSpacePortId",
			"departureSchedule" : "$outboundDepartureSchedule",
			"arrivalSpacePortId" : "$arrivalSpacePortId"
        }]
}
"""
    private val oneWayCriteriaOnSamePlanet = """
{
	"journeys" : [{
			"departureSpacePortId" : "$departureSpacePortId",
			"departureSchedule" : "$outboundDepartureSchedule",
			"arrivalSpacePortId" : "$departureSpacePortId"
        }]
}
"""

    private val roundTripCriteria = """
{
	"journeys" : [
                    {
			            "departureSpacePortId" : "$departureSpacePortId",
			            "departureSchedule" : "$outboundDepartureSchedule",
			            "arrivalSpacePortId" : "$arrivalSpacePortId"
                    },
                    {
                    	"departureSpacePortId" : "$arrivalSpacePortId",
			            "departureSchedule" : "$inboundDepartureSchedule",
			            "arrivalSpacePortId" : "$departureSpacePortId"
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
                .accept(HAL_JSON)
                .contentType(HAL_JSON)
                .content(oneWayCriteria)
        )
            //
            .andExpect(status().isCreated)
            .andExpect(content().contentType(HAL_JSON))
            .andExpect(jsonPath("$.criteria.journeys").value(hasSize<Collection<*>>(1)))
            .andExpect(jsonPath("$.criteria.journeys[0].departureSpacePortId").value(departureSpacePortId))
            .andExpect(jsonPath("$.criteria.journeys[0].departureSchedule").value(outboundDepartureSchedule.toString()))
            .andExpect(jsonPath("$.criteria.journeys[0].arrivalSpacePortId").value(arrivalSpacePortId))
            .andExpect(jsonPath("$.spaceTrains").doesNotHaveJsonPath())
            .andDo { location = it.response.getHeader("Location")!! }
            .andExpect(jsonPath("$._links.self.href").value(location))
            .andExpect(jsonPath("$._links.all-outbounds.href").value("$location/spacetrains?bound=OUTBOUND"))
            .andExpect(jsonPath("$._links.all-inbounds").doesNotHaveJsonPath())

        checkOutboundSpaceTrains(location)

        mvc.perform(
            get("$location/spacetrains?bound=INBOUND")
                .accept(HAL_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.spaceTrains").value(hasSize<Collection<*>>(0)))


    }

    private fun checkOutboundSpaceTrains(location: String) {
        mvc.perform(
            get("$location/spacetrains")
                .accept(HAL_JSON)
                .queryParam("bound", "OUTBOUND")
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.spaceTrains").value(hasSize<Collection<*>>(5)))
            .andExpect(jsonPath("$.spaceTrains[0].number").value(notNullValue()))
            .andExpect(jsonPath("$.spaceTrains[0].bound").value("OUTBOUND"))
            .andExpect(jsonPath("$.spaceTrains[0].originId").value(departureSpacePortId))
            .andExpect(jsonPath("$.spaceTrains[0].destinationId").value(arrivalSpacePortId))
            .andExpect(jsonPath("$.spaceTrains[0].departureSchedule").exists())
            .andExpect(jsonPath("$.spaceTrains[0].arrivalSchedule").exists())
            .andExpect(jsonPath("$.spaceTrains[0].duration").exists())
            .andExpect(jsonPath("$._links.self.href").value("$location/spacetrains?bound=OUTBOUND"))
            .andExpect(jsonPath("$._links.search.href").value(location))
    }

    private fun checkInboundSpaceTrains(location: String, onlySelectable: Boolean = false) {
        mvc.perform(
            get("$location/spacetrains?bound=INBOUND")
                .accept(HAL_JSON)
                .queryParam("bound", "OUTBOUND")
                .queryParam("onlySelectable", onlySelectable.toString())
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.spaceTrains[0].number").value(notNullValue()))
            .andExpect(jsonPath("$.spaceTrains[0].bound").value("INBOUND"))
            .andExpect(jsonPath("$.spaceTrains[0].departureSchedule").exists())
            .andExpect(jsonPath("$.spaceTrains[0].arrivalSchedule").exists())
            .andExpect(jsonPath("$.spaceTrains[0].duration").exists())
            .andExpect(jsonPath("$.spaceTrains[0].originId").value(arrivalSpacePortId))
            .andExpect(jsonPath("$.spaceTrains[0].number").value(notNullValue()))
            .andExpect(jsonPath("$.spaceTrains[0].destinationId").value(departureSpacePortId))
            .andExpect(jsonPath("$._links.self.href").value("$location/spacetrains?bound=INBOUND"))
            .andExpect(jsonPath("$._links.search.href").value(location))
    }

    @Test
    fun `return an error when try to go from and to the same astronomical body`() {
        mvc.perform(
            post("/searches")
                .accept(HAL_JSON)
                .contentType(HAL_JSON)
                .content(oneWayCriteriaOnSamePlanet)
        )
            //
            .andExpect(status().isBadRequest)
            .andExpect(status().reason("Cannot perform a trip departing and arriving on the same Planet"))
    }

    @Test
    fun `perform a search satisfying some roundtrip criteria`() {
        lateinit var location: String
        mvc.perform(
            post("/searches")
                .accept(HAL_JSON)
                .contentType(HAL_JSON)
                .content(roundTripCriteria)
        )
            //
            .andExpect(status().isCreated)
            .andExpect(content().contentType(HAL_JSON))
            .andExpect(jsonPath("$.criteria.journeys").value(hasSize<Collection<*>>(2)))
            .andExpect(jsonPath("$.criteria.journeys[0].departureSpacePortId").value(departureSpacePortId))
            .andExpect(jsonPath("$.criteria.journeys[0].departureSchedule").value(outboundDepartureSchedule.toString()))
            .andExpect(jsonPath("$.criteria.journeys[0].arrivalSpacePortId").value(arrivalSpacePortId))
            .andExpect(jsonPath("$.criteria.journeys[1].departureSpacePortId").value(arrivalSpacePortId))
            .andExpect(jsonPath("$.criteria.journeys[1].departureSchedule").value(inboundDepartureSchedule.toString()))
            .andExpect(jsonPath("$.criteria.journeys[1].arrivalSpacePortId").value(departureSpacePortId))
            .andExpect(jsonPath("$.spaceTrains").doesNotHaveJsonPath())
            .andDo { location = it.response.getHeader("Location")!! }
            .andExpect(jsonPath("$._links.self.href").value(location))
            .andExpectCorrectAllBoundsLinks(location)
        checkOutboundSpaceTrains(location)
        checkInboundSpaceTrains(location)

    }

    @Test
    fun `perform a complete workflow on a round trip search`(@RoundTrip search: Search) {
        lateinit var location: String
        lateinit var allOutbounds: String
        lateinit var allInbounds: String

        mvc.perform(
            get("/searches/${search.id}")
                .accept(HAL_JSON)
        )
            //
            .andExpect(status().isOk)
            .andExpect(content().contentType(HAL_JSON))
            .andDo { location = JsonPath.read(it.response.contentAsString, "$._links.self.href") }
            .andExpectCorrectAllBoundsLinks(location)
            .andDo { allOutbounds = JsonPath.read(it.response.contentAsString, "$._links.all-outbounds.href") }
            .andDo { allInbounds = JsonPath.read(it.response.contentAsString, "$._links.all-inbounds.href") }

        mvc.perform(
            get(allOutbounds)
                .accept(HAL_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(HAL_JSON))
            .andExpect(jsonPath("$.spaceTrains").value(hasSize<Collection<*>>(search.spaceTrains[OUTBOUND].size)))
            .andExpect(jsonPath("$._links.self.href").value(allOutbounds))

        mvc.perform(
            get(allInbounds)
                .accept(HAL_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(HAL_JSON))
    }

    @Test
    fun `return 404 when search id is unknown`() {
        mvc.perform(
            get("/searches/8ca7339a-4fc7-4b08-ad49-593d3ff12e03")
                .accept(HAL_JSON)
        )
            .andExpect(status().isNotFound)
    }
}

private fun ResultActions.andExpectCorrectAllBoundsLinks(location: String): ResultActions =
    andExpect(jsonPath("$._links.all-outbounds.href").value("$location/spacetrains?bound=OUTBOUND"))
        .andExpect(jsonPath("$._links.all-inbounds.href").value("$location/spacetrains?bound=INBOUND"))