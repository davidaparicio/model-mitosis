package org.craftsrecords.columbiadexpress.domain.search.selection

import org.assertj.core.api.Assertions.assertThat
import org.craftsrecords.columbiadexpress.domain.EqualityShould
import org.craftsrecords.columbiadexpress.domain.Random
import org.craftsrecords.columbiadexpress.domain.sharedkernel.Bound
import org.craftsrecords.columbiadexpress.domain.sharedkernel.Bound.OUTBOUND
import org.junit.jupiter.api.Test

class SelectionShould : EqualityShould<Selection> {
    @Test
    fun `compute the total price of the selection`(
        @Random outboundSpaceTrain: SelectedSpaceTrain,
        @Random inboundSpaceTrain: SelectedSpaceTrain
    ) {
        val selection = Selection(mapOf(OUTBOUND to outboundSpaceTrain, Bound.INBOUND to inboundSpaceTrain))

        assertThat(selection.totalPrice).isEqualTo(outboundSpaceTrain.price + inboundSpaceTrain.price)
    }

    @Test
    fun `not have a total price if no space trains have been selected`() {
        val selection = Selection()
        assertThat(selection.totalPrice).isNull()
    }

    @Test
    fun `indicates there is no selection for a not selected bound`() {
        val selection = Selection()
        assertThat(selection.hasASelectionFor(OUTBOUND)).isFalse
    }

    @Test
    fun `indicates there is a selection for a selected bound`() {
        val selection = Selection(mapOf(OUTBOUND to selectedSpaceTrain()))
        assertThat(selection.hasASelectionFor(OUTBOUND)).isTrue
    }
}