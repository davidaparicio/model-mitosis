package com.beyondxscratch.mandaloreexpress.domain

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.math.BigDecimal.ONE
import java.math.BigDecimal.TEN
import java.math.BigDecimal.ZERO
import java.util.Currency
import java.util.Locale.FRANCE
import java.util.Locale.US

class PriceShould : EqualityShould<Price> {
    @Test
    fun `not be null`() {
        assertThatThrownBy { Price(ZERO, Currency.getInstance(FRANCE)) }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("Price cannot be negative")
    }

    @Test
    fun `not be strictly negative`() {
        assertThatThrownBy { Price(BigDecimal(-3), Currency.getInstance(FRANCE)) }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("Price cannot be negative")
    }

    @Test
    fun `not be added to another price if currency don't match`() {
        assertThatThrownBy { Price(TEN, Currency.getInstance(FRANCE)) + Price(TEN, Currency.getInstance(US)) }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("Cannot sum prices with different currencies")
    }

    @Test
    fun `sum prices`() {
        val currency = Currency.getInstance(FRANCE)
        val price = Price(TEN, currency) + Price(ONE, currency)
        assertThat(price.amount).isEqualTo(BigDecimal(11))
        assertThat(price.currency).isEqualTo(currency)

    }
}