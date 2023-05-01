package com.beyondxscratch.mandaloreexpress.domain.spacetrain.fare

import com.beyondxscratch.mandaloreexpress.domain.EqualityShould
import com.beyondxscratch.mandaloreexpress.domain.spacetrain.fare.Currency.CALAMARI_FLAN
import com.beyondxscratch.mandaloreexpress.domain.spacetrain.fare.Currency.REPUBLIC_CREDIT
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import java.math.BigDecimal
import java.math.BigDecimal.*

class PriceShould : EqualityShould<Price> {
    @Test
    fun `not be null`() {
        assertThatThrownBy { Price(ZERO, REPUBLIC_CREDIT) }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("Price must be strictly positive")
    }

    @Test
    fun `not be strictly negative`() {
        assertThatThrownBy { Price(BigDecimal(-3), REPUBLIC_CREDIT) }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("Price must be strictly positive")
    }

    @Test
    fun `not be added to another price if currency don't match`() {
        assertThatThrownBy { Price(TEN, REPUBLIC_CREDIT) + Price(TEN, CALAMARI_FLAN) }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("Cannot sum prices with different currencies")
    }

    @Test
    fun `sum prices`() {
        val currency = REPUBLIC_CREDIT
        val price = Price(TEN, currency) + Price(ONE, currency)
        assertThat(price.amount).isEqualTo(BigDecimal(11))
        assertThat(price.currency).isEqualTo(currency)

    }

    @ParameterizedTest
    @EnumSource(value = Currency::class, names = ["REPUBLIC_CREDIT", "CALAMARI_FLAN"])
    fun `accept Republican Credits and Calamari Flans`(currency: Currency) {
        assertThatCode { Price(TEN, currency) }.doesNotThrowAnyException()
    }

    @ParameterizedTest
    @EnumSource(value = Currency::class, names = ["IMPERIAL_CREDIT", "WUPIUPI"])
    fun `refuse Imperial Credits and Outer Rim currencies`(currency: Currency) {
        assertThatThrownBy { Price(TEN, currency) }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("Republic credits will do fine.")
    }
}