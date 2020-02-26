package org.craftsrecords.columbiadexpress.domain.search.selection

import org.craftsrecords.columbiadexpress.domain.search.SpaceTrain
import org.craftsrecords.columbiadexpress.domain.sharedkernel.Bound
import org.craftsrecords.columbiadexpress.domain.sharedkernel.Price
import java.util.UUID

data class Selection(private val selectedSpaceTrainsByBound: Map<Bound, SelectedSpaceTrain> = mapOf()) : Map<Bound, SelectedSpaceTrain> by selectedSpaceTrainsByBound {

    val bounds = keys
    val spaceTrains = values
    val spaceTrainsByBound = entries

    val totalPrice: Price?
        get() =
            spaceTrains
                    .map { it.price }
                    .takeIf { it.isNotEmpty() }
                    ?.reduce(Price::plus)

    fun selectSpaceTrainWithFare(spaceTrain: SpaceTrain, fareId: UUID, price: Price): Selection {
        val newSelectedSpaceTrains =
                selectedSpaceTrainsByBound + (spaceTrain.bound to SelectedSpaceTrain(spaceTrain.number, fareId, price))
        return Selection(newSelectedSpaceTrains)
    }

    fun hasASelectionFor(bound: Bound): Boolean = bounds.contains(bound)
}



