package com.beyondxscratch.mandaloreexpress.domain

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

interface EqualityShould<T> {

    @Test
    fun `be equal to the same value`(value: T, otherValue: T) {
        assertThat(value).isEqualTo(otherValue)
        assertThat(value).hasSameHashCodeAs(otherValue)
    }

    @Test
    fun `not be equal to a different value`(value: T, @Random otherValue: T) {
        assertThat(value).isNotEqualTo(otherValue)
        assertThat(value.hashCode()).isNotEqualTo(otherValue.hashCode())
    }
}
