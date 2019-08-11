package org.craftsrecords.columbiadexpress.domain

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

@InjectDomainObjects
interface EqualityShould<T> {

    @Test
    fun `be equal to the same value`(value: T, otherValue: T) {
        assertThat(value).isEqualTo(otherValue)
        assertThat(value).hasSameHashCodeAs(otherValue)
    }

    @Test
    fun `not be equal to a different value value`(value: T, @Random otherValue: T) {
        assertThat(value).isNotEqualTo(otherValue)
        assertThat(value.hashCode()).isNotEqualTo(otherValue.hashCode())
    }
}
