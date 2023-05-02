package com.beyondxscratch.mandaloreexpress.domain.spacetrain.fare

import com.beyondxscratch.mandaloreexpress.domain.EqualityShould
import com.beyondxscratch.mandaloreexpress.domain.spacetrain.fare.Currency.CALAMARI_FLAN
import com.beyondxscratch.mandaloreexpress.domain.spacetrain.fare.Currency.REPUBLIC_CREDIT
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.math.BigDecimal.*

class PriceShould : EqualityShould<Price> {

    @Test
    fun `not be added to another price if currency don't match`(amount: Amount) {
        assertThatThrownBy { Price(amount, REPUBLIC_CREDIT) + Price(amount, CALAMARI_FLAN) }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("Cannot sum prices with different currencies")
    }

    @Test
    fun `sum prices`() {
        val currency = REPUBLIC_CREDIT
        val tenCredits = Price(Amount(TEN), currency)
        val oneCredit = Price(Amount(ONE), currency)

        val elevenCredits = Price(Amount(BigDecimal(11)), currency)

        assertThat(tenCredits + oneCredit).isEqualTo(elevenCredits)
    }

    @Test
    fun `not sum prices on different currencies`() {
        val tenCredits = Price(Amount(TEN), REPUBLIC_CREDIT)
        val oneCalamariFlan = Price(Amount(ONE), CALAMARI_FLAN)

        assertThatThrownBy { tenCredits + oneCalamariFlan }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("Cannot sum prices with different currencies")
    }

    @Test
    fun `apply a discount`() {
        val currency = REPUBLIC_CREDIT

        val tenCredits = Price(Amount(TEN), currency)
        val oneCreditDiscount = Discount(Amount(ONE), currency)
        val nineCredits = Price(Amount(BigDecimal(9)), currency)

        val discountedPrice = tenCredits.apply(oneCreditDiscount)

        assertThat(discountedPrice).isEqualTo(nineCredits)
    }

    @Test
    fun `not apply a discount with the wrong currency`(amount: Amount) {
        val credits = Price(amount, REPUBLIC_CREDIT)
        val calamariFlanDiscount = Discount(amount, CALAMARI_FLAN)

        assertThatThrownBy { credits.apply(calamariFlanDiscount) }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("Cannot sum prices with different currencies")
    }
}