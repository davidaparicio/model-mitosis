package com.beyondxscratch.mandaloreexpress.domain.booking.tax

import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class TaxRateShould {

    @ParameterizedTest
    @ValueSource(strings = ["-0.5", "0.0"])
    fun `not be negative`(value : String){
        assertThatThrownBy { TaxRate(value.toBigDecimal()) }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("TaxRate must be strictly positive")
    }
}