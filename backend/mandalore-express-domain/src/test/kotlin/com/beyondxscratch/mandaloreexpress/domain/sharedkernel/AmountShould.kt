package com.beyondxscratch.mandaloreexpress.domain.sharedkernel

import com.beyondxscratch.mandaloreexpress.domain.EqualityShould
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class AmountShould : EqualityShould<Amount> {

    @Test
    fun `not be null`() {
        assertThatThrownBy { Amount(BigDecimal.ZERO) }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("Amount must be strictly positive")
    }

    @Test
    fun `not be strictly negative`() {
        assertThatThrownBy { amount(-3) }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("Amount must be strictly positive")
    }

    @Test
    fun `add amounts`() {
        val ten = Amount(BigDecimal.TEN)
        val one = Amount(BigDecimal.ONE)

        val eleven = amount(11)

        assertThat(ten + one).isEqualTo(eleven)
    }

    @Test
    fun `subtract amounts`() {
        val ten = Amount(BigDecimal.TEN)
        val one = Amount(BigDecimal.ONE)

        val nine = amount(9)

        assertThat(ten - one).isEqualTo(nine)
    }

    @Test
    fun `compare amount to find the highest`() {
        val ten = Amount(BigDecimal.TEN)
        val one = Amount(BigDecimal.ONE)

        assertThat(ten > one).isEqualTo(true)
    }

    @Test
    fun `compare amount to find the lowest`() {
        val ten = Amount(BigDecimal.TEN)
        val one = Amount(BigDecimal.ONE)

        assertThat(one < ten).isEqualTo(true)
    }

    @Test
    fun `compare amount equality`() {
        val ten = Amount(BigDecimal.TEN)

        assertThat(ten >= ten).isEqualTo(true)
        assertThat(ten <= ten).isEqualTo(true)
    }

    @Test
    fun `multiply amount by a scalar`() {
        val ten = Amount(BigDecimal.TEN)
        val five = BigDecimal(5.0)

        val fifty = ten * five

        assertThat(fifty).isEqualTo(amount(50.0))
    }
}