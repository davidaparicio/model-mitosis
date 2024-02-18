package com.beyondxscratch.mandaloreexpress.domain.booking.tax

import com.beyondxscratch.mandaloreexpress.domain.EqualityShould
import com.beyondxscratch.mandaloreexpress.domain.sharedkernel.OneCalamariFlan
import com.beyondxscratch.mandaloreexpress.domain.sharedkernel.OneRepCredit
import com.beyondxscratch.mandaloreexpress.domain.sharedkernel.TenRepCredit
import com.beyondxscratch.mandaloreexpress.domain.sharedkernel.amount
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class TaxPortionShould : EqualityShould<TaxPortion> {

    @Test
    fun `not be added to another tax portion if currency don't match`(
        @OneCalamariFlan oneCalamariFlan: TaxPortion,
        @OneRepCredit oneRepCredit: TaxPortion
    ) {
        Assertions.assertThatThrownBy { oneCalamariFlan + oneRepCredit }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("Cannot sum tax portions with different currencies")
    }

    @Test
    fun `sum prices`(@TenRepCredit tenRepCredits: TaxPortion, @OneRepCredit oneRepCredit: TaxPortion) {

        val elevenCredits = TaxPortion(amount(11), tenRepCredits.currency)

        Assertions.assertThat(tenRepCredits + oneRepCredit).isEqualTo(elevenCredits)
    }

    @Test
    fun `not sum tax portions on different currencies`(
        @TenRepCredit tenRepCredits: TaxPortion,
        @OneCalamariFlan oneCalamariFlan: TaxPortion
    ) {

        Assertions.assertThatThrownBy { tenRepCredits + oneCalamariFlan }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("Cannot sum tax portions with different currencies")
    }
}