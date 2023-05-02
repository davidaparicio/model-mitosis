package com.beyondxscratch.mandaloreexpress.domain.spacetrain.fare

import com.beyondxscratch.mandaloreexpress.domain.EqualityShould
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class AmountShould: EqualityShould<Amount> {

    @Test
    fun `not be null`() {
        assertThatThrownBy { Amount(BigDecimal.ZERO) }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("Amount must be strictly positive")
    }

    @Test
    fun `not be strictly negative`() {
        assertThatThrownBy { Amount(BigDecimal(-3)) }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("Amount must be strictly positive")
    }

    @Test
    fun `should add amounts`(){
        val ten = Amount(BigDecimal.TEN)
        val one = Amount(BigDecimal.ONE)

        val eleven = Amount(BigDecimal(11))

        assertThat(ten + one).isEqualTo(eleven)
    }

    @Test
    fun `should subtract amounts`(){
        val ten = Amount(BigDecimal.TEN)
        val one = Amount(BigDecimal.ONE)

        val nine = Amount(BigDecimal(9))

        assertThat(ten - one).isEqualTo(nine)
    }

}