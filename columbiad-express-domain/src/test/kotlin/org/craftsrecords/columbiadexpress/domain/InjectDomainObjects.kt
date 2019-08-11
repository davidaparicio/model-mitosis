package org.craftsrecords.columbiadexpress.domain

import org.junit.jupiter.api.extension.ExtendWith
import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.CLASS


@Retention(RUNTIME)
@Target(CLASS)
@ExtendWith(SpacePortParameterResolver::class)
annotation class InjectDomainObjects