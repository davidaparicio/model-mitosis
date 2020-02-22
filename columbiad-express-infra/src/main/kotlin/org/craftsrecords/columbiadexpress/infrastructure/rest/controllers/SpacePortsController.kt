package org.craftsrecords.columbiadexpress.infrastructure.rest.controllers

import org.craftsrecords.columbiadexpress.domain.api.RetrieveSpacePorts
import org.craftsrecords.columbiadexpress.infrastructure.rest.resources.spaceport.SpacePort
import org.craftsrecords.columbiadexpress.infrastructure.rest.resources.spaceport.SpacePorts
import org.craftsrecords.columbiadexpress.infrastructure.rest.resources.spaceport.toResource
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpServletResponse.SC_NOT_FOUND

@RestController
@RequestMapping("/spaceports")
class SpacePortsController(private val retrieveSpacePorts: RetrieveSpacePorts) {


    companion object {
        const val WHATEVER = ""
    }

    @GetMapping
    fun getSpacePorts(@RequestParam(name = "withNameContaining", required = false) partialName: String?): SpacePorts {
        val spacePorts = retrieveSpacePorts `having in their name` (partialName ?: WHATEVER)
        return spacePorts
                .toResource()
                .addLinks(partialName)
    }

    @GetMapping(path = ["/{id}"])
    fun getSpacePortIdentifiedBy(@PathVariable id: String): SpacePort {
        val spacePort = retrieveSpacePorts `identified by` id
        return spacePort.toResource()
    }

    @ExceptionHandler(NoSuchElementException::class)
    fun handleNoSuchElementException(response: HttpServletResponse, exception: NoSuchElementException) {
        response.sendError(SC_NOT_FOUND, exception.message)
    }
}