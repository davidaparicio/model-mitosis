package org.craftsrecords.columbiadexpress.infrastructure.rest.controllers

import org.craftsrecords.columbiadexpress.infrastructure.configurations.DomainConfiguration
import org.hamcrest.Matchers.hasSize
import org.hamcrest.Matchers.notNullValue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDateTime.now
import java.time.temporal.ChronoUnit.MINUTES

@WebMvcTest(controllers = [SearchController::class])
@Import(DomainConfiguration::class)
class SearchControllerShould(@Autowired val mvc: MockMvc) {

    private val departureSpacePort = "http://localhost/spaceports/ddf86b0b-94e3-3566-8486-fd076b9686a6"
    private val arrivalSpacePort = "http://localhost/spaceports/4ed3116c-e359-3245-b8d0-cec742551507"
    private val departureSchedule =
            now().plusDays(5)
                    .withHour(10)
                    .withMinute(0)
                    .truncatedTo(MINUTES)
    private val criteria = """
{
	"journeys" : [{
			"departureSpacePort" : "$departureSpacePort",
			"departureSchedule" : "$departureSchedule",
			"arrivalSpacePort" : "$arrivalSpacePort"
        }]
}
"""

    @Test
    fun `perform a search satisfying some criteria`() {
        mvc.perform(
                post("/searches")
                        .accept(APPLICATION_JSON_VALUE)
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(criteria))
                //
                .andExpect(status().isCreated)
                .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.criteria.journeys").value(hasSize<Collection<*>>(1)))
                .andExpect(jsonPath("$.criteria.journeys[0].departureSpacePort").value(departureSpacePort))
                .andExpect(jsonPath("$.criteria.journeys[0].departureSchedule").value(departureSchedule.toString()))
                .andExpect(jsonPath("$.criteria.journeys[0].arrivalSpacePort").value(arrivalSpacePort))
                .andExpect(jsonPath("$.spaceTrains").value(hasSize<Collection<*>>(3)))
                .andExpect(jsonPath("$.spaceTrains[0].number").value(notNullValue()))
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
                .andExpect(jsonPath("$.spaceTrains[0].fares[1].comfortClass").value("SECOND"))
                .andExpect(jsonPath("$.spaceTrains[0].fares[1].price.amount").value(notNullValue()))
                .andExpect(jsonPath("$.spaceTrains[0].fares[1].price.currency").value("EUR"))
                .andExpect { jsonPath("$._links.self.href").value(it.response.getHeaderValue("Location")!!) }

    }
}