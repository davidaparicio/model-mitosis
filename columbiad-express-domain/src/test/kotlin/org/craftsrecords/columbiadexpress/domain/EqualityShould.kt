package org.craftsrecords.columbiadexpress.domain

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

interface EqualityShould<T> {

    fun createValue(): T
    fun createAnotherValue(): T

    @Test
    fun `be equal to the same value`() {
        val value = createValue()
        val otherValue = createValue()

        assertThat(value).isEqualTo(otherValue)
        assertThat(value).hasSameHashCodeAs(otherValue)
    }

    @Test
    fun `not be equal to a different value value`() {
        val value = createValue()
        val otherValue = createAnotherValue()

        assertThat(value).isNotEqualTo(otherValue)
        assertThat(value.hashCode()).isNotEqualTo(otherValue.hashCode())
    }
}
