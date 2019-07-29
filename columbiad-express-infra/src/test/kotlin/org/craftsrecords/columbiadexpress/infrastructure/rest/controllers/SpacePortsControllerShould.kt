package org.craftsrecords.columbiadexpress.infrastructure.rest.controllers

import org.craftsrecords.columbiadexpress.infrastructure.configurations.DomainConfiguration
import org.hamcrest.Matchers.equalTo
import org.hamcrest.collection.IsCollectionWithSize
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType.APPLICATION_JSON_UTF8
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@ExtendWith(SpringExtension::class)
@WebMvcTest(controllers = [SpacePortsController::class])
@Import(DomainConfiguration::class)
class SpacePortsControllerShould {

    @Autowired
    private lateinit var mvc: MockMvc

    @Test
    fun `list all available Space Ports`() {
        mvc.perform(
                get("/spaceports")
                        .accept(APPLICATION_JSON_UTF8))
                //
                .andExpect(status().isOk)
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.spacePorts", IsCollectionWithSize<Any>(equalTo(3))))
    }
}