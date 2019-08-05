package org.craftsrecords.columbiadexpress.infrastructure.rest.controllers

import org.craftsrecords.columbiadexpress.infrastructure.configurations.DomainConfiguration
import org.hamcrest.Matchers.containsInAnyOrder
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType.APPLICATION_JSON_UTF8
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@ExtendWith(SpringExtension::class)
@WebMvcTest(controllers = [SpacePortsController::class])
@Import(DomainConfiguration::class)
class SpacePortsControllerShould {

    @Autowired
    private lateinit var mvc: MockMvc

    @Test
    fun `list all available SpacePorts`() {
        mvc.perform(
                get("/spaceports")
                        .accept(APPLICATION_JSON_UTF8))
                //
                .andExpect(status().isOk)
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.spacePorts.length()").value(3))
                .andExpect(jsonPath("$.spacePorts[0].id").isNotEmpty)
                .andExpect(jsonPath("$.spacePorts[0].name").value("Vostochny Cosmodrome"))
                .andExpect(jsonPath("$.spacePorts[0].location").value("EARTH"))
    }

    @Test
    fun `list all SpacePorts having a name containing a given string`() {
        mvc.perform(
                get("/spaceports")
                        .param("withNameContaining", "Cosmo")
                        .accept(APPLICATION_JSON_UTF8))
                //
                .andExpect(status().isOk)
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.spacePorts[*].name").value(containsInAnyOrder("Vostochny Cosmodrome", "Baikonur Cosmodrome")))
    }
}