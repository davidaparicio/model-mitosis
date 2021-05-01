package org.craftsrecords.columbiadexpress.infrastructure.rest.controllers

import org.craftsrecords.columbiadexpress.infrastructure.configurations.DomainConfiguration
import org.hamcrest.Matchers.containsInAnyOrder
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.hateoas.MediaTypes.HAL_JSON_VALUE
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.header
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.UUID.nameUUIDFromBytes

@WebMvcTest(controllers = [SpacePortsController::class])
@Import(DomainConfiguration::class)
class SpacePortsControllerShould(@Autowired val mvc: MockMvc) {

    @Test
    fun `list all available SpacePorts`() {
        mvc.perform(
            get("/spaceports")
                .accept(HAL_JSON_VALUE)
        )
            //
            .andExpect(status().isOk)
            .andExpect(header().exists("Cache-Control"))
            .andExpect(content().contentType(HAL_JSON_VALUE))
            .andExpect(jsonPath("$._embedded.spacePorts.length()").value(10))
            .andExpect(jsonPath("$._links.self.href").value("http://localhost/spaceports{?withNameContaining}"))
            .andExpect(jsonPath("$._links.self.templated").value(true))

    }

    @Test
    fun `list all SpacePorts having a name containing a given string`() {
        val partialName = "Cosmo"
        mvc.perform(
            get("/spaceports")
                .param("withNameContaining", partialName)
                .accept(HAL_JSON_VALUE)
        )
            //
            .andExpect(status().isOk)
            .andExpect(content().contentType(HAL_JSON_VALUE))
            .andExpect(
                jsonPath("$._embedded.spacePorts[*].name").value(
                    containsInAnyOrder(
                        "Vostochny Cosmodrome",
                        "Baikonur Cosmodrome"
                    )
                )
            )
            .andExpect(jsonPath("$._links.self.href").value("http://localhost/spaceports?withNameContaining=$partialName"))
            .andExpect(jsonPath("$._links.spaceports.href").value("http://localhost/spaceports{?withNameContaining}"))
            .andExpect(jsonPath("$._links.spaceports.templated").value(true))
    }

    @Test
    fun `give the details of a specific SpacePort`() {
        val spacePortId = nameUUIDFromBytes("Vostochny".toByteArray()).toString()
        mvc.perform(
            get("/spaceports/{id}", spacePortId)
        )
            //
            .andExpect(status().isOk)
            .andExpect(header().exists("Cache-Control"))
            .andExpect(content().contentType(HAL_JSON_VALUE))
            .andExpect(jsonPath("$.name").value("Vostochny Cosmodrome"))
            .andExpect(jsonPath("$.location").value("EARTH"))
            .andExpect(jsonPath("$._links.self.href").value("http://localhost/spaceports/$spacePortId"))
    }

    @Test
    fun `return 404 if the SpacePort doesn't exist`() {
        val spacePortId = "unknown"

        mvc.perform(
            get("/spaceports/{id}", spacePortId)
        )
            //
            .andExpect(status().isNotFound)
    }
}