package com.beyondxscratch.mandaloreexpress.domain.booking.spacetrain

import com.beyondxscratch.mandaloreexpress.domain.sharedkernel.fare.tenRepCreditsPrice
import com.beyondxscratch.mandaloreexpress.domain.sharedkernel.randomSchedule
import com.beyondxscratch.mandaloreexpress.domain.sharedkernel.schedule
import java.util.UUID
import kotlin.random.Random.Default.nextLong
import kotlin.reflect.KClass
import kotlin.reflect.full.declaredMembers


fun spaceTrain(): SpaceTrain = SpaceTrain(
    number = "6127",
    originId = "CORUSCANT",
    destinationId = "MANDALORE",
    schedule = schedule(),
    fare = fare()
)

inline fun <reified T> randomFare(): T {
    val comfortClass = randomComfortClass<T>()
    return T::class.constructors.first().call(UUID.randomUUID(), comfortClass, tenRepCreditsPrice())
}

inline fun <reified T> fare(): T {
    val comfortClass = firstComfortClass<T>()
    return T::class.constructors.first()
        .call(UUID.nameUUIDFromBytes("fare1".toByteArray()), comfortClass, tenRepCreditsPrice())
}

inline fun <reified T> firstComfortClass(): Any? =
    (T::class.declaredMembers.find { it.name == "comfortClass" }?.returnType?.classifier as KClass<*>).java.enumConstants.first()
inline fun <reified T> randomComfortClass(): Any? =
    (T::class.declaredMembers.find { it.name == "comfortClass" }?.returnType?.classifier as KClass<*>).java.enumConstants.random()
inline fun <reified T> firstClassFare(): T = fare()

fun randomSpaceTrain(): SpaceTrain = spaceTrain()
    .copy(
        number = nextLong(1, 1000).toString(),
        schedule = randomSchedule(),
        fare = randomFare()
    )

fun SpaceTrain.withFirstClass(): SpaceTrain {
    return this.copy(fare = firstClassFare())
}

fun SpaceTrain.numbered(number: String): SpaceTrain {
    return this.copy(number = number)
}