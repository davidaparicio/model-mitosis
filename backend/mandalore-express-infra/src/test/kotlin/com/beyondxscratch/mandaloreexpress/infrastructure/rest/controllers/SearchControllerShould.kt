package com.beyondxscratch.mandaloreexpress.infrastructure.rest.controllers

import com.beyondxscratch.mandaloreexpress.domain.search.OneWay
import com.beyondxscratch.mandaloreexpress.domain.search.RoundTrip
import com.beyondxscratch.mandaloreexpress.domain.search.Search
import com.beyondxscratch.mandaloreexpress.domain.search.spaceport.OnCoruscant
import com.beyondxscratch.mandaloreexpress.domain.search.spaceport.OnMandalore
import com.beyondxscratch.mandaloreexpress.domain.search.spaceport.SpacePort
import com.beyondxscratch.mandaloreexpress.domain.search.spacetrain.Bound.INBOUND
import com.beyondxscratch.mandaloreexpress.domain.search.spacetrain.Bound.OUTBOUND
import com.beyondxscratch.mandaloreexpress.domain.search.spacetrain.SpaceTrain.Companion.get
import com.beyondxscratch.mandaloreexpress.domain.search.spi.Searches
import com.beyondxscratch.mandaloreexpress.infrastructure.configurations.DomainConfiguration
import com.jayway.jsonpath.JsonPath
import org.hamcrest.Matchers.endsWith
import org.hamcrest.Matchers.hasSize
import org.hamcrest.Matchers.matchesPattern
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
            .andExpect(jsonPath("$._links.selection.href").value("$location/selection"))
            .andExpect(jsonPath("$._links.all-outbounds.href").value("$location/spacetrains?bound=OUTBOUND&onlySelectable=false"))
            .andExpect(jsonPath("$._links.all-inbounds").doesNotHaveJsonPath())
            .andExpect(jsonPath("$._links.outbounds-for-current-selection").doesNotHaveJsonPath())
            .andExpect(jsonPath("$._links.inbounds-for-current-selection").doesNotHaveJsonPath())
            .andExpect(jsonPath("$._links.create-booking").doesNotHaveJsonPath())

        checkOutboundSpaceTrains(location)

        mvc.perform(
            get("$location/spacetrains?bound=INBOUND")
                .accept(HAL_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.spaceTrains").value(hasSize<Collection<*>>(0)))


    }

    private fun checkOutboundSpaceTrains(location: String, onlySelectable: Boolean = false) {
        lateinit var spaceTrainNumber: String
        mvc.perform(
            get("$location/spacetrains")
                .accept(HAL_JSON)
                .queryParam("bound", "OUTBOUND")
                .queryParam("onlySelectable", onlySelectable.toString())
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.spaceTrains").value(hasSize<Collection<*>>(5)))
            .andExpect(jsonPath("$.spaceTrains[0].number").value(notNullValue()))
            .andDo { spaceTrainNumber = JsonPath.read(it.response.contentAsString, "$.spaceTrains[0].number") }
            .andExpect(jsonPath("$.spaceTrains[0].bound").value("OUTBOUND"))
            .andExpect(jsonPath("$.spaceTrains[0].originId").value(departureSpacePortId))
            .andExpect(jsonPath("$.spaceTrains[0].destinationId").value(arrivalSpacePortId))
            .andExpect(jsonPath("$.spaceTrains[0].departureSchedule").exists())
            .andExpect(jsonPath("$.spaceTrains[0].arrivalSchedule").exists())
            .andExpect(jsonPath("$.spaceTrains[0].duration").exists())
            .andExpect(jsonPath("$.spaceTrains[0].fareOptions").value(hasSize<Collection<*>>(2)))
            .andExpect(jsonPath("$.spaceTrains[0].fareOptions[0].comfortClass").value("FIRST"))
            .andExpect(jsonPath("$.spaceTrains[0].fareOptions[0].price.amount").value(notNullValue()))
            .andExpect(jsonPath("$.spaceTrains[0].fareOptions[0].price.currency").value("REPUBLIC_CREDIT"))
            .andExpect(jsonPath("$.spaceTrains[0].fareOptions[0]._links.select.href").value(matchesPattern("$location/spacetrains/$spaceTrainNumber/fareoptions/.*/select\\?resetSelection=${!onlySelectable}")))
            .andExpect(jsonPath("$._links.self.href").value("$location/spacetrains?bound=OUTBOUND&onlySelectable=$onlySelectable"))
            .andExpect(jsonPath("$._links.search.href").value(location))
    }

    private fun checkInboundSpaceTrains(location: String, onlySelectable: Boolean = false) {
        lateinit var spaceTrainNumber: String
        mvc.perform(
            get("$location/spacetrains?bound=INBOUND")
                .accept(HAL_JSON)
                .queryParam("bound", "OUTBOUND")
                .queryParam("onlySelectable", onlySelectable.toString())
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.spaceTrains[0].number").value(notNullValue()))
            .andDo { spaceTrainNumber = JsonPath.read(it.response.contentAsString, "$.spaceTrains[0].number") }
            .andExpect(jsonPath("$.spaceTrains[0].bound").value("INBOUND"))
            .andExpect(jsonPath("$.spaceTrains[0].departureSchedule").exists())
            .andExpect(jsonPath("$.spaceTrains[0].arrivalSchedule").exists())
            .andExpect(jsonPath("$.spaceTrains[0].duration").exists())
            .andExpect(jsonPath("$.spaceTrains[0].originId").value(arrivalSpacePortId))
            .andExpect(jsonPath("$.spaceTrains[0].number").value(notNullValue()))
            .andExpect(jsonPath("$.spaceTrains[0].destinationId").value(departureSpacePortId))
            .andExpect(jsonPath("$.spaceTrains[0].fareOptions").value(hasSize<Collection<*>>(2)))
            .andExpect(jsonPath("$.spaceTrains[0].fareOptions[0].comfortClass").value("FIRST"))
            .andExpect(jsonPath("$.spaceTrains[0].fareOptions[0].price.amount").value(notNullValue()))
            .andExpect(jsonPath("$.spaceTrains[0].fareOptions[0].price.currency").value("REPUBLIC_CREDIT"))
            .andExpect(jsonPath("$.spaceTrains[0].fareOptions[0]._links.select.href").value(matchesPattern("$location/spacetrains/$spaceTrainNumber/fareoptions/.*/select\\?resetSelection=${!onlySelectable}")))
            .andExpect(jsonPath("$._links.self.href").value("$location/spacetrains?bound=INBOUND&onlySelectable=$onlySelectable"))
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
            .andExpect(jsonPath("$._links.selection.href").value("$location/selection"))
            .andExpectCorrectAllBoundsLinks(location)
            .andExpect(jsonPath("$._links.outbounds-for-current-selection").doesNotHaveJsonPath())
            .andExpect(jsonPath("$._links.inbounds-for-current-selection").doesNotHaveJsonPath())
        checkOutboundSpaceTrains(location)
        checkInboundSpaceTrains(location)

        mvc.perform(
            get("$location/selection")
                .accept(HAL_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.spaceTrains").value(hasSize<Collection<*>>(0)))
            .andExpect(jsonPath("$.totalPrice").doesNotHaveJsonPath())
            .andExpect(jsonPath("$._links.search.href").value(location))
            .andExpect(jsonPath("$._links.self.href").value("$location/selection"))
            .andExpect(jsonPath("$._links.create-booking").doesNotHaveJsonPath())

    }

    @Test
    fun `perform a complete workflow on a round trip search`(@RoundTrip search: Search) {
        lateinit var location: String
        lateinit var allOutbounds: String
        lateinit var allInbounds: String
        lateinit var firstOutboundSelect: String
        lateinit var otherOutboundSelect: String
        lateinit var selectableInbounds: String
        lateinit var selectableOutbounds: String
        lateinit var otherInboundSelect: String
        lateinit var firstInboundSelect: String
        lateinit var getSearch: String

        mvc.perform(
            get("/searches/${search.id}")
                .accept(HAL_JSON)
        )
            //
            .andExpect(status().isOk)
            .andExpect(content().contentType(HAL_JSON))
            .andDo { location = JsonPath.read(it.response.contentAsString, "$._links.self.href") }
            .andExpectCorrectAllBoundsLinks(location)
            .andExpect(jsonPath("$._links.outbounds-for-current-selection").doesNotHaveJsonPath())
            .andExpect(jsonPath("$._links.inbounds-for-current-selection").doesNotHaveJsonPath())
            .andExpect(jsonPath("$._links.create-booking").doesNotHaveJsonPath())
            .andDo { allOutbounds = JsonPath.read(it.response.contentAsString, "$._links.all-outbounds.href") }

        mvc.perform(
            get(allOutbounds)
                .accept(HAL_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(HAL_JSON))
            .andExpect(jsonPath("$._links.outbounds-for-current-selection").doesNotHaveJsonPath())
            .andExpect(jsonPath("$._links.inbounds-for-current-selection").doesNotHaveJsonPath())
            .andExpect(jsonPath("$.spaceTrains").value(hasSize<Collection<*>>(search.spaceTrains[OUTBOUND].size)))
            .andExpect(jsonPath("$._links.selection").exists())
            .andExpect(jsonPath("$._links.self.href").value(allOutbounds))
            .andExpect(jsonPath("$.spaceTrains[0].fareOptions[0]._links.select.href").value(endsWith("resetSelection=true")))
            .andDo {
                firstOutboundSelect =
                    JsonPath.read(it.response.contentAsString, "$.spaceTrains[0].fareOptions[0]._links.select.href")
            }

        mvc.perform(
            post(firstOutboundSelect)
                .accept(HAL_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(HAL_JSON))
            .andExpect(jsonPath("$.spaceTrains").value(hasSize<Collection<*>>(1)))
            .andExpect(jsonPath("$._links.self.href").value("$location/selection"))
            .andExpectCorrectAllBoundsLinks(location)
            .andExpect(jsonPath("$._links.inbounds-for-current-selection.href").exists())
            .andExpect(jsonPath("$._links.selection").exists())
            .andExpect(jsonPath("$._links.outbounds-for-current-selection").doesNotHaveJsonPath())
            .andExpect(jsonPath("$._links.create-booking").doesNotHaveJsonPath())
            .andDo {
                selectableInbounds =
                    JsonPath.read(it.response.contentAsString, "$._links.inbounds-for-current-selection.href")
            }

        mvc.perform(
            get(selectableInbounds)
                .accept(HAL_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(HAL_JSON))
            .andExpect(jsonPath("$.spaceTrains").value(hasSize<Collection<*>>(search.spaceTrains.first().compatibleSpaceTrains.size)))
            .andExpect(jsonPath("$._links.selection").exists())
            .andExpect(jsonPath("$._links.self.href").value(selectableInbounds))
            .andExpect(jsonPath("$.spaceTrains[0].fareOptions[0]._links.select.href").value(endsWith("resetSelection=false")))
            .andDo {
                firstInboundSelect =
                    JsonPath.read(it.response.contentAsString, "$.spaceTrains[0].fareOptions[0]._links.select.href")
            }

        mvc.perform(
            post(firstInboundSelect)
                .accept(HAL_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(HAL_JSON))
            .andExpect(jsonPath("$.spaceTrains").value(hasSize<Collection<*>>(2)))
            .andExpect(jsonPath("$._links.self.href").value("$location/selection"))
            .andExpectCorrectAllBoundsLinks(location)
            .andExpect(jsonPath("$._links.inbounds-for-current-selection").exists())
            .andExpect(jsonPath("$._links.selection").exists())
            .andExpect(jsonPath("$._links.outbounds-for-current-selection").exists())
            .andExpect(jsonPath("$._links.create-booking.href").exists())
            .andDo { getSearch = JsonPath.read(it.response.contentAsString, "$._links.search.href") }

        mvc.perform(
            get(getSearch)
                .accept(HAL_JSON)
        )
            .andExpectCorrectAllBoundsLinks(location)
            .andExpect(jsonPath("$._links.inbounds-for-current-selection").exists())
            .andExpect(jsonPath("$._links.outbounds-for-current-selection").exists())
            .andExpect(jsonPath("$._links.create-booking.href").exists())
            .andDo { allInbounds = JsonPath.read(it.response.contentAsString, "$._links.all-inbounds.href") }


        mvc.perform(
            get(allInbounds)
                .accept(HAL_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(HAL_JSON))
            .andExpect(jsonPath("$._links.outbounds-for-current-selection").doesNotHaveJsonPath())
            .andExpect(jsonPath("$._links.inbounds-for-current-selection").doesNotHaveJsonPath())
            .andExpect(jsonPath("$._links.create-booking").doesNotHaveJsonPath())
            .andExpect(jsonPath("$.spaceTrains[1].fareOptions[0]._links.select.href").value(endsWith("resetSelection=true")))
            .andDo {
                otherInboundSelect =
                    JsonPath.read(it.response.contentAsString, "$.spaceTrains[1].fareOptions[0]._links.select.href")
            }


        mvc.perform(
            post(otherInboundSelect)
                .accept(HAL_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(HAL_JSON))
            .andExpect(jsonPath("$.spaceTrains").value(hasSize<Collection<*>>(1)))
            .andExpect(jsonPath("$._links.self.href").value("$location/selection"))
            .andExpectCorrectAllBoundsLinks(location)
            .andExpect(jsonPath("$._links.inbounds-for-current-selection").doesNotHaveJsonPath())
            .andExpect(jsonPath("$._links.outbounds-for-current-selection").exists())
            .andExpect(jsonPath("$._links.create-booking.href").doesNotHaveJsonPath())
            .andDo {
                selectableOutbounds =
                    JsonPath.read(it.response.contentAsString, "$._links.outbounds-for-current-selection.href")
            }

        mvc.perform(
            get(selectableOutbounds)
                .accept(HAL_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(HAL_JSON))
            .andExpect(jsonPath("$.spaceTrains").value(hasSize<Collection<*>>(search.spaceTrains[INBOUND][1].compatibleSpaceTrains.size)))
            .andExpect(jsonPath("$._links.selection").exists())
            .andExpect(jsonPath("$._links.self.href").value(selectableOutbounds))
            .andExpect(jsonPath("$.spaceTrains[0].fareOptions[0]._links.select.href").value(endsWith("resetSelection=false")))
            .andDo {
                otherOutboundSelect =
                    JsonPath.read(it.response.contentAsString, "$.spaceTrains[0].fareOptions[0]._links.select.href")
            }

        mvc.perform(
            post(otherOutboundSelect)
                .accept(HAL_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(HAL_JSON))
            .andExpect(jsonPath("$.spaceTrains").value(hasSize<Collection<*>>(2)))
            .andExpect(jsonPath("$._links.self.href").value("$location/selection"))
            .andExpectCorrectAllBoundsLinks(location)
            .andExpect(jsonPath("$._links.inbounds-for-current-selection").exists())
            .andExpect(jsonPath("$._links.outbounds-for-current-selection").exists())
            .andExpect(jsonPath("$._links.create-booking.href").exists())


    }

    @Test
    fun `select space trains with fare options`(@RoundTrip search: Search) {
        val outBoundSpaceTrain = search.spaceTrains.first { it.bound == OUTBOUND }
        val outBoundFare = outBoundSpaceTrain.fareOptions.first()
        val inBoundSpaceTrain = search.spaceTrains.first { it.bound == INBOUND }
        val inBoundFare = inBoundSpaceTrain.fareOptions.first()
        val location = "http://localhost/searches/${search.id}"

        val selectedSpaceTrainsAndFares =
            arrayListOf(outBoundSpaceTrain to outBoundFare, inBoundSpaceTrain to inBoundFare)

        mvc.perform(
            post("/searches/${search.id}/spacetrains/${inBoundSpaceTrain.number}/fareoptions/${inBoundFare.id}/select")
                .accept(HAL_JSON)
        )
            .andExpect(jsonPath("$._links.create-booking").doesNotHaveJsonPath())
            .andExpectCorrectAllBoundsLinks(location)

        mvc.perform(
            post("/searches/${search.id}/spacetrains/${outBoundSpaceTrain.number}/fareoptions/${outBoundFare.id}/select")
                .accept(HAL_JSON)
        )
            //
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.spaceTrains").value(hasSize<Collection<*>>(2)))
            .also { result ->
                selectedSpaceTrainsAndFares
                    .forEachIndexed { index, spaceTrainFare ->
                        val (spaceTrain, fare) = spaceTrainFare
                        result.andExpect(jsonPath("$.spaceTrains[$index].bound").value(spaceTrain.bound.toString()))
                            .andExpect(jsonPath("$.spaceTrains[$index].number").value(spaceTrain.number))
                            .andExpect(jsonPath("$.spaceTrains[$index].originId").value(spaceTrain.originId))
                            .andExpect(jsonPath("$.spaceTrains[$index].destinationId").value(spaceTrain.destinationId))
                            .andExpect(jsonPath("$.spaceTrains[$index].fareOption.comfortClass").value(fare.comfortClass.toString()))
                            .andExpect(jsonPath("$.spaceTrains[$index].fareOption.price.amount").value(fare.price.amount.value))
                            .andExpect(jsonPath("$.spaceTrains[$index].fareOption.price.currency").value(fare.price.currency.toString()))

                    }
            }

            .andExpect(jsonPath("$.totalPrice.amount").value(outBoundFare.price.amount.value + inBoundFare.price.amount.value))
            .andExpect(jsonPath("$.totalPrice.currency").value(outBoundFare.price.currency.toString()))
            .andExpect(jsonPath("$._links.search.href").value(location))
            .andExpect(jsonPath("$._links.self.href").value("$location/selection"))
            .andExpectCorrectAllBoundsLinks(location)
            .andExpect(jsonPath("$._links.create-booking.href").value("http://localhost/bookings?searchId=${search.id}"))

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
    andExpect(jsonPath("$._links.all-outbounds.href").value("$location/spacetrains?bound=OUTBOUND&onlySelectable=false"))
        .andExpect(jsonPath("$._links.all-inbounds.href").value("$location/spacetrains?bound=INBOUND&onlySelectable=false"))