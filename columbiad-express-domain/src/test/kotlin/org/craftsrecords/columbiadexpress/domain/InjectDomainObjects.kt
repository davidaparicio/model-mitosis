package org.craftsrecords.columbiadexpress.domain

import org.craftsrecords.columbiadexpress.domain.search.criteria.JourneyParameterResolver
import org.craftsrecords.columbiadexpress.domain.spaceport.SpacePortParameterResolver
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.CLASS
import kotlin.annotation.AnnotationTarget.FILE

@Retention(RUNTIME)
@Target(CLASS, FILE)
@ExtendWith(SpacePortParameterResolver::class, JourneyParameterResolver::class)
annotation class InjectDomainObjects
