package org.craftsrecords.columbiadexpress.domain.search

import java.math.BigDecimal
import java.math.BigDecimal.TEN
import java.util.Currency.getInstance
import java.util.Locale.FRANCE
import kotlin.random.Random.Default.nextDouble

fun createPrice(): Price = Price(TEN,  getInstance(FRANCE))
fun createRandomPrice(): Price = Price(BigDecimal(nextDouble(500.0, 1000.0)),  getInstance(FRANCE))