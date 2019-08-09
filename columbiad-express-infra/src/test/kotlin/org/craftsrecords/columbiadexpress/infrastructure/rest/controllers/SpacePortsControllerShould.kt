package org.craftsrecords.columbiadexpress.infrastructure.rest.controllers

import org.craftsrecords.columbiadexpress.infrastructure.configurations.DomainConfiguration
import org.hamcrest.Matchers.containsInAnyOrder
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.hateoas.MediaTypes.HAL_JSON_VALUE
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.UUID.nameUUIDFromBytes

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
                        .accept(APPLICATION_JSON_VALUE))
                //
                .andExpect(status().isOk)
                .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$._embedded.spacePorts.length()").value(3))
                .andExpect(jsonPath("$._links.self.href").value("http://localhost/spaceports"))
                .andExpect(jsonPath("$._links.allSpacePorts.href").value("http://localhost/spaceports"))

    }

    @Test
    fun `list all SpacePorts having a name containing a given string`() {
        val partialName = "Cosmo"
        mvc.perform(
                get("/spaceports")
                        .param("withNameContaining", partialName)
                        .accept(APPLICATION_JSON_VALUE))
                //
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk)
                .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$._embedded.spacePorts[*].name").value(containsInAnyOrder("Vostochny Cosmodrome", "Baikonur Cosmodrome")))
                .andExpect(jsonPath("$._links.self.href").value("http://localhost/spaceports?withNameContaining=$partialName"))
                .andExpect(jsonPath("$._links.allSpacePorts.href").value("http://localhost/spaceports"))
    }

    @Test
    fun `give the details of a specific SpacePort`() {
        val spacePortId = nameUUIDFromBytes("1".toByteArray())
        mvc.perform(
                get("/spaceports/{id}", spacePortId))
                //
                .andExpect(status().isOk)
                .andExpect(content().contentType(HAL_JSON_VALUE))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.name").value("Vostochny Cosmodrome"))
                .andExpect(jsonPath("$.location").value("EARTH"))
                .andExpect(jsonPath("$._links.self.href").value("http://localhost/spaceports/$spacePortId"))
    }

    @Test
    fun `return 404 if the SpacePort doesn't exist`() {
        val spacePortId = nameUUIDFromBytes("unknown".toByteArray())

        mvc.perform(
                get("/spaceports/{id}", spacePortId))
                //
                .andExpect(status().isNotFound)
    }
}