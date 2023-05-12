package com.beyondxscratch.mandaloreexpress.domain.spacetrain.fare

import com.beyondxscratch.mandaloreexpress.domain.EqualityShould
import com.beyondxscratch.mandaloreexpress.domain.sharedkernel.fare.Price
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

class PriceShould : EqualityShould<Price> {

    @Test
    fun `not be added to another price if currency don't match`(
        @OneCalamariFlan oneCalamariFlan: Price,
        @OneRepCredit oneRepCredit: Price
    ) {
        assertThatThrownBy { oneCalamariFlan + oneRepCredit }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("Cannot sum prices with different currencies")
    }

    @Test
    fun `sum prices`(@TenRepCredit tenRepCredits: Price, @OneRepCredit oneRepCredit: Price) {

        val elevenCredits = Price(amount(11), tenRepCredits.currency)

        assertThat(tenRepCredits + oneRepCredit).isEqualTo(elevenCredits)
    }

    @Test
    fun `not sum prices on different currencies`(
        @TenRepCredit tenRepCredits: Price,
        @OneCalamariFlan oneCalamariFlan: Price
    ) {

        assertThatThrownBy { tenRepCredits + oneCalamariFlan }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("Cannot sum prices with different currencies")
    }
}