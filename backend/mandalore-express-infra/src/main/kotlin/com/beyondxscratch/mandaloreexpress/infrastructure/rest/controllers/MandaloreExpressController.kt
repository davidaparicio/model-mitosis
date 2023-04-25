package com.beyondxscratch.mandaloreexpress.infrastructure.rest.controllers

import com.beyondxscratch.mandaloreexpress.infrastructure.rest.resources.Resource
import org.springframework.hateoas.IanaLinkRelations.SELF
import org.springframework.hateoas.RepresentationModel
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.hateoas.server.mvc.add
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/")
class MandaloreExpressController {

    @GetMapping
    fun root(): MandaloreExpress {
        return MandaloreExpress()
                .add(linkTo(SearchController::class.java).withRel("searches"))
                .add(linkTo(BookingController::class.java).withRel("bookings"))
                .add(MandaloreExpressController::class) {
                    linkTo { root() } withRel SELF
                }
                .add(SpacePortsController::class) {
                    linkTo { getSpacePorts(null) } withRel "spaceports"
                }

    }

}

@Resource
class MandaloreExpress : RepresentationModel<MandaloreExpress>()