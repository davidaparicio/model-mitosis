package org.craftsrecords.columbiadexpress.domain.search

import org.assertj.core.api.Assertions.assertThatThrownBy
import org.craftsrecords.columbiadexpress.domain.EqualityShould
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.math.BigDecimal.ZERO
import java.util.Currency
import java.util.Locale.FRANCE

class PriceShould : EqualityShould<Price>{
    @Test
    fun `not be null`(){
        assertThatThrownBy { Price(ZERO, Currency.getInstance(FRANCE)) }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("Price cannot be negative")
    }

    @Test
    fun `not be strictly negative`(){
        assertThatThrownBy { Price(BigDecimal(-3), Currency.getInstance(FRANCE)) }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("Price cannot be negative")
    }
}