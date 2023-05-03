package com.beyondxscratch.mandaloreexpress.domain.spacetrain.fare

import com.beyondxscratch.mandaloreexpress.domain.EqualityShould
import com.beyondxscratch.mandaloreexpress.domain.spacetrain.fare.ComfortClass.FIRST
import com.beyondxscratch.mandaloreexpress.domain.spacetrain.fare.Currency.REPUBLIC_CREDIT
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

class FareShould : EqualityShould<Fare> {
    @Test
    fun `not have a discount higher than the price`(
        @OneRepCredit oneCredit: Price,
        @TenRepCredit tenCreditDiscount: Discount
    ) {
        assertThatThrownBy { Fare(comfortClass = FIRST, basePrice = oneCredit, discount = tenCreditDiscount) }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("Cannot have a discount higher than the base price")
    }

    @Test
    fun `not apply a discount when there is none`(@TenRepCredit tenCredits: Price) {
        val fare = Fare(comfortClass = FIRST, basePrice = tenCredits)

        assertThat(fare.price).isEqualTo(tenCredits)
    }

    @Test
    fun `applies the discount when there is one`(@TenRepCredit tenCredits: Price, @OneRepCredit oneCredit: Discount) {
        val fare = Fare(comfortClass = FIRST, basePrice = tenCredits, discount = oneCredit)

        assertThat(fare.price).isEqualTo(Price(amount(9), REPUBLIC_CREDIT))
    }
}