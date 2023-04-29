package com.beyondxscratch.mandaloreexpress.infrastructure.rest.controllers

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.hateoas.MediaTypes.HAL_JSON_VALUE
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(controllers = [MandaloreExpressController::class])
class MandaloreExpressControllerShould(@Autowired val mvc: MockMvc) {

    @Test
    fun `give all the available endpoints`() {

        val rootLocation = "http://localhost"
        mvc.perform(
                get("/")
                        .accept(HAL_JSON_VALUE))
                .andExpect(status().isOk)
                .andExpect(jsonPath("$._links.self.href").value("$rootLocation/"))
                .andExpect(jsonPath("$._links.spaceports.href").value("$rootLocation/spaceports{?withNameContaining}"))
                .andExpect(jsonPath("$._links.spaceports.templated").value(true))
                .andExpect(jsonPath("$._links.searches.href").value("$rootLocation/searches"))
                .andDo(print())
    }
}