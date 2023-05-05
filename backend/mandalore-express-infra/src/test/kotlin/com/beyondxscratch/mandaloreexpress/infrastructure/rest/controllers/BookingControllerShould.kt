package com.beyondxscratch.mandaloreexpress.infrastructure.rest.controllers

import com.beyondxscratch.mandaloreexpress.domain.search.Booking
import com.beyondxscratch.mandaloreexpress.domain.annotations.OneWay
import com.beyondxscratch.mandaloreexpress.domain.annotations.RoundTrip
import com.beyondxscratch.mandaloreexpress.domain.search.Search
import com.beyondxscratch.mandaloreexpress.domain.search.selectAnInboundSpaceTrain
import com.beyondxscratch.mandaloreexpress.domain.search.selectAnOutboundSpaceTrain
import com.beyondxscratch.mandaloreexpress.domain.search.spi.Bookings
import com.beyondxscratch.mandaloreexpress.domain.search.spi.Searches
import com.beyondxscratch.mandaloreexpress.infrastructure.configurations.DomainConfiguration
import org.hamcrest.Matchers.hasSize
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.hateoas.MediaTypes.HAL_JSON
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.UUID.nameUUIDFromBytes

@WebMvcTest(controllers = [BookingController::class])
@Import(DomainConfiguration::class)
class BookingControllerShould(
    @Autowired val mvc: MockMvc,
    @Autowired val bookings: Bookings,
    @Autowired val searches: Searches
) {

    @Test
    fun `create a booking from a selection`(@RoundTrip roundTripSearch: Search) {
        val (searchWithPartialSelection, inboundSpaceTrain, inboundFare) = roundTripSearch.selectAnInboundSpaceTrain()
        val (searchWithSelection, outboundSpaceTrain, outboundFare) = searchWithPartialSelection.selectAnOutboundSpaceTrain()

        searches.save(searchWithSelection)

        lateinit var location: String
        mvc.perform(
            post("/bookings")
                .queryParam("searchId", searchWithSelection.id.toString())
                .accept(HAL_JSON)
                .contentType(HAL_JSON)
        )
            //
            .andExpect(status().isCreated)
            .andExpect(content().contentType(HAL_JSON))
            .andDo { location = it.response.getHeader("Location")!! }
            .andExpect(jsonPath("$._links.self.href").value(location))
            .andExpect(jsonPath("$.spaceTrains").value(hasSize<Collection<*>>(2)))
            .andExpect(jsonPath("$.spaceTrains[0].number").value(outboundSpaceTrain.number))
            .andExpect(jsonPath("$.spaceTrains[0].originId").value(outboundSpaceTrain.originId))
            .andExpect(jsonPath("$.spaceTrains[0].destinationId").value(outboundSpaceTrain.destinationId))
            .andExpect(jsonPath("$.spaceTrains[0].departureSchedule").exists())
            .andExpect(jsonPath("$.spaceTrains[0].arrivalSchedule").exists())
            .andExpect(jsonPath("$.spaceTrains[0].fare.comfortClass").value(outboundFare.comfortClass.name))
            .andExpect(jsonPath("$.spaceTrains[0].fare.price.amount").value(outboundFare.price.amount.value))
            .andExpect(jsonPath("$.spaceTrains[0].fare.price.currency").value(outboundFare.price.currency.toString()))

            .andExpect(jsonPath("$.spaceTrains[1].number").value(inboundSpaceTrain.number))
            .andExpect(jsonPath("$.spaceTrains[1].originId").value(inboundSpaceTrain.originId))
            .andExpect(jsonPath("$.spaceTrains[1].destinationId").value(inboundSpaceTrain.destinationId))
            .andExpect(jsonPath("$.spaceTrains[1].departureSchedule").exists())
            .andExpect(jsonPath("$.spaceTrains[1].arrivalSchedule").exists())
            .andExpect(jsonPath("$.spaceTrains[1].fare.comfortClass").value(inboundFare.comfortClass.name))
            .andExpect(jsonPath("$.spaceTrains[1].fare.price.amount").value(inboundFare.price.amount.value))
            .andExpect(jsonPath("$.spaceTrains[1].fare.price.currency").value(inboundFare.price.currency.toString()))

            .andExpect(jsonPath("$.totalPrice.amount").isNumber)
            .andExpect(jsonPath("$.totalPrice.currency").value("REPUBLIC_CREDIT"))
    }

    @Test
    fun `not create a booking from an unknown search id`() {
        val unknownId = nameUUIDFromBytes("unknown".toByteArray()).toString()
        mvc.perform(
            post("/bookings")
                .queryParam("searchId", unknownId)
                .accept(HAL_JSON)
                .contentType(HAL_JSON)
        )
            .andExpect(status().isBadRequest)
            .andExpect(status().reason("cannot find any search corresponding to id $unknownId"))
    }

    @Test
    fun `not create a booking from an incomplete selection`(@OneWay search: Search) {
        searches.save(search)

        mvc.perform(
            post("/bookings")
                .queryParam("searchId", search.id.toString())
                .accept(HAL_JSON)
                .contentType(HAL_JSON)
        )
            .andExpect(status().isBadRequest)
            .andExpect(status().reason("cannot book a partial selection"))
    }

    @Test
    fun `retrieve an existing booking`(booking: Booking) {
        bookings.save(booking)

        mvc.perform(
            get("/bookings/${booking.id}")
                .accept(HAL_JSON)
                .contentType(HAL_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$._links.self.href").value("http://localhost/bookings/${booking.id}"))
    }

    @Test
    fun `not retrieve a booking from an unknwon id`() {
        val unknownId = nameUUIDFromBytes("unknown".toByteArray()).toString()

        mvc.perform(
            get("/bookings/${unknownId}")
                .accept(HAL_JSON)
                .contentType(HAL_JSON)
        )
            .andExpect(status().isNotFound)
    }

}
