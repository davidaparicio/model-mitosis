package com.beyondxscratch.mandaloreexpress.infrastructure.rest.controllers

import com.beyondxscratch.mandaloreexpress.domain.booking.CannotBookAPartialSelection
import com.beyondxscratch.mandaloreexpress.domain.booking.api.PrepareBooking
import com.beyondxscratch.mandaloreexpress.domain.booking.spi.Bookings
import com.beyondxscratch.mandaloreexpress.infrastructure.rest.resources.booking.Booking
import com.beyondxscratch.mandaloreexpress.infrastructure.rest.resources.booking.toResource
import org.springframework.hateoas.IanaLinkRelations.SELF
import org.springframework.hateoas.server.EntityLinks
import org.springframework.hateoas.server.ExposesResourceFor
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.created
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import java.util.UUID
import com.beyondxscratch.mandaloreexpress.domain.booking.Booking as DomainBooking

@RestController
@RequestMapping("/bookings")
@ExposesResourceFor(Booking::class)
class BookingController(
    private val prepareBooking: PrepareBooking,
    private val bookings: Bookings,
    private val entityLinks: EntityLinks
) {

    @PostMapping
    fun bookSomeSpaceTrainsFromTheSelectionOf(@RequestParam searchId: UUID): ResponseEntity<Booking> {
        return try {
            val domainBooking = prepareBooking `from the selection of` searchId
            val booking = domainBooking.toResource()
            created(booking.getRequiredLink(SELF).toUri()).body(booking)
        } catch (exception: NoSuchElementException) {
            throw ResponseStatusException(BAD_REQUEST, "cannot find any search corresponding to id $searchId")
        } catch (exception: CannotBookAPartialSelection) {
            throw ResponseStatusException(BAD_REQUEST, exception.message)
        }
    }

    @GetMapping("/{bookingId}")
    fun retrieveAnExistingBooking(@PathVariable bookingId: UUID): Booking {
        val domainBooking = bookings `find booking identified by` bookingId ?: throw ResponseStatusException(NOT_FOUND)
        return domainBooking.toResource()
    }

    private fun DomainBooking.toResource(): Booking {
        val bookingLink = entityLinks.linkForItemResource(Booking::class.java, id)
        return Booking(id, spaceTrains.toResource(), totalPrice.toResource())
            .add(bookingLink.withSelfRel())
    }
}