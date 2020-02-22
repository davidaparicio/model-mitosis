package org.craftsrecords.columbiadexpress.infrastructure.rest.controllers

import org.craftsrecords.columbiadexpress.domain.booking.Booking
import org.craftsrecords.columbiadexpress.domain.search.OneWay
import org.craftsrecords.columbiadexpress.domain.search.RoundTrip
import org.craftsrecords.columbiadexpress.domain.search.Search
import org.craftsrecords.columbiadexpress.domain.search.selectAnInboundSpaceTrain
import org.craftsrecords.columbiadexpress.domain.search.selectAnOutboundSpaceTrain
import org.craftsrecords.columbiadexpress.domain.spi.Bookings
import org.craftsrecords.columbiadexpress.domain.spi.Searches
import org.craftsrecords.columbiadexpress.infrastructure.configurations.DomainConfiguration
import org.hamcrest.Matchers.hasSize
import org.hamcrest.Matchers.notNullValue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.UUID.nameUUIDFromBytes

@WebMvcTest(controllers = [BookingController::class])
@Import(DomainConfiguration::class)
class BookingControllerShould(@Autowired val mvc: MockMvc, @Autowired val bookings: Bookings, @Autowired val searches: Searches) {

    @Test
    fun `create a booking from a selection`(@RoundTrip roundTripSearch: Search) {
        val (searchWithPartialSelection, inboundSpaceTrain, inboundFare) = roundTripSearch.selectAnInboundSpaceTrain()
        val (searchWithSelection, outboundSpaceTrain, outboundFare) = searchWithPartialSelection.selectAnOutboundSpaceTrain()

        searches.save(searchWithSelection)

        lateinit var location: String
        mvc.perform(
                post("/bookings")
                        .queryParam("searchId", searchWithSelection.id.toString())
                        .accept(APPLICATION_JSON_VALUE)
                        .contentType(APPLICATION_JSON_VALUE))
                //
                .andExpect(status().isCreated)
                .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                .andDo { location = it.response.getHeader("Location")!! }
                .andExpect(jsonPath("$._links.self.href").value(location))
                .andExpect(jsonPath("$.spaceTrains").value(hasSize<Collection<*>>(2)))
                .andExpect(jsonPath("$.spaceTrains[0].number").value(outboundSpaceTrain.number))
                .andExpect(jsonPath("$.spaceTrains[0].origin.name").value(outboundSpaceTrain.origin.name))
                .andExpect(jsonPath("$.spaceTrains[0].origin.location").value(outboundSpaceTrain.origin.location.name))
                .andExpect(jsonPath("$.spaceTrains[0].origin._links.self.href").value(notNullValue()))
                .andExpect(jsonPath("$.spaceTrains[0].destination.name").value(outboundSpaceTrain.destination.name))
                .andExpect(jsonPath("$.spaceTrains[0].destination.location").value(outboundSpaceTrain.destination.location.name))
                .andExpect(jsonPath("$.spaceTrains[0].destination._links.self.href").value(notNullValue()))
                .andExpect(jsonPath("$.spaceTrains[0].departureSchedule").exists())
                .andExpect(jsonPath("$.spaceTrains[0].arrivalSchedule").exists())
                .andExpect(jsonPath("$.spaceTrains[0].fare.comfortClass").value(outboundFare.comfortClass.name))
                .andExpect(jsonPath("$.spaceTrains[0].fare.price.amount").value(outboundFare.price.amount))
                .andExpect(jsonPath("$.spaceTrains[0].fare.price.currency").value(outboundFare.price.currency.toString()))

                .andExpect(jsonPath("$.spaceTrains[1].number").value(inboundSpaceTrain.number))
                .andExpect(jsonPath("$.spaceTrains[1].origin.name").value(inboundSpaceTrain.origin.name))
                .andExpect(jsonPath("$.spaceTrains[1].origin.location").value(inboundSpaceTrain.origin.location.name))
                .andExpect(jsonPath("$.spaceTrains[1].origin._links.self.href").value(notNullValue()))
                .andExpect(jsonPath("$.spaceTrains[1].destination.name").value(inboundSpaceTrain.destination.name))
                .andExpect(jsonPath("$.spaceTrains[1].destination.location").value(inboundSpaceTrain.destination.location.name))
                .andExpect(jsonPath("$.spaceTrains[1].destination._links.self.href").value(notNullValue()))
                .andExpect(jsonPath("$.spaceTrains[1].departureSchedule").exists())
                .andExpect(jsonPath("$.spaceTrains[1].arrivalSchedule").exists())
                .andExpect(jsonPath("$.spaceTrains[1].fare.comfortClass").value(inboundFare.comfortClass.name))
                .andExpect(jsonPath("$.spaceTrains[1].fare.price.amount").value(inboundFare.price.amount))
                .andExpect(jsonPath("$.spaceTrains[1].fare.price.currency").value(inboundFare.price.currency.toString()))
    }

    @Test
    fun `not create a booking from an unknown search id`() {
        val unknownId = nameUUIDFromBytes("unknown".toByteArray()).toString()
        mvc.perform(
                post("/bookings")
                        .queryParam("searchId", unknownId)
                        .accept(APPLICATION_JSON_VALUE)
                        .contentType(APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest)
                .andExpect(status().reason("cannot find any search corresponding to id $unknownId"))
    }

    @Test
    fun `not create a booking from an incomplete selection`(@OneWay search: Search) {
        searches.save(search)

        mvc.perform(
                post("/bookings")
                        .queryParam("searchId", search.id.toString())
                        .accept(APPLICATION_JSON_VALUE)
                        .contentType(APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest)
                .andExpect(status().reason("cannot book a partial selection"))
    }

    @Test
    fun `retrieve an existing booking`(booking: Booking) {
        bookings.save(booking)

        mvc.perform(
                get("/bookings/${booking.id}")
                        .accept(APPLICATION_JSON_VALUE)
                        .contentType(APPLICATION_JSON_VALUE))
                .andExpect(status().isOk)
                .andExpect(jsonPath("$._links.self.href").value("http://localhost/bookings/${booking.id}"))
    }

    @Test
    fun `not retrieve a booking from an unknwon id`() {
        val unknownId = nameUUIDFromBytes("unknown".toByteArray()).toString()

        mvc.perform(
                get("/bookings/${unknownId}")
                        .accept(APPLICATION_JSON_VALUE)
                        .contentType(APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound)
    }

}
