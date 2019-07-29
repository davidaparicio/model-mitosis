package org.craftsrecords.columbiadexpress.infrastructure.rest.controllers

import org.craftsrecords.columbiadexpress.domain.spaceport.api.RetrieveSpacePorts
import org.craftsrecords.columbiadexpress.infrastructure.rest.resources.SpacePorts
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/spaceports")
class SpacePortsController(private val retrieveSpacePorts: RetrieveSpacePorts) {

    @GetMapping
    fun `retrieve all spaceports`(): SpacePorts {
        val spacePorts = retrieveSpacePorts `having in their name` ""
        return SpacePorts(spacePorts)
    }
}