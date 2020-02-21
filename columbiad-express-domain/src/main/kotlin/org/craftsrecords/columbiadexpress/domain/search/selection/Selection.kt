package org.craftsrecords.columbiadexpress.domain.search.selection

import org.craftsrecords.columbiadexpress.domain.search.SpaceTrain
import org.craftsrecords.columbiadexpress.domain.sharedkernel.Bound
import org.craftsrecords.columbiadexpress.domain.sharedkernel.Price
import java.util.UUID

data class Selection(val selectedSpaceTrains: Map<Bound, SelectedSpaceTrain> = mapOf()) {

    val totalPrice: Price?
        get() =
            selectedSpaceTrains.values
                    .map { it.price }
                    .takeIf { it.isNotEmpty() }
                    ?.reduce(Price::plus)

    fun selectSpaceTrainWithFare(spaceTrain: SpaceTrain, fareId: UUID, price: Price): Selection {
        val newSelectedSpaceTrains =
                selectedSpaceTrains
                        .plus(spaceTrain.bound to SelectedSpaceTrain(spaceTrain.number, fareId, price))
        return Selection(newSelectedSpaceTrains)
    }
}



