package com.beyondxscratch.mandaloreexpress.domain.spacetrain.fare

import com.beyondxscratch.mandaloreexpress.domain.EqualityShould
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Nested
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

    @Test
    fun `apply a discount`(@TenRepCredit tenRepCredits: Price, @OneRepCredit oneRepCreditDiscount: Discount) {

        val nineCredits = tenRepCredits.copy(amount = amount(9))

        assertThat(tenRepCredits.apply(oneRepCreditDiscount)).isEqualTo(nineCredits)
    }

    @Test
    fun `not apply a discount with the wrong currency`(
        @OneCalamariFlan oneCalamariFlan: Price,
        @OneRepCredit oneRepCreditDiscount: Discount
    ) {

        assertThatThrownBy { oneCalamariFlan.apply(oneRepCreditDiscount) }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("Cannot sum prices with different currencies")
    }

    @Test
    fun `return basePrise if there is no discount`(@OneCalamariFlan oneCalamariFlan: Price) {

        assertThat (oneCalamariFlan.apply(null)).isEqualTo(oneCalamariFlan)
    }
}