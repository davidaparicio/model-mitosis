package org.craftsrecords.columbiadexpress.domain.search.selection

import org.craftsrecords.columbiadexpress.domain.search.Bound
import org.craftsrecords.columbiadexpress.domain.search.SpaceTrain
import java.util.*

data class Selection(val selectedSpaceTrains: Map<Bound, SelectedSpaceTrain> = mapOf()) {

    fun selectSpaceTrainWithFare(spaceTrain: SpaceTrain, fareId: UUID): Selection {
        val newSelectedSpaceTrains =
                selectedSpaceTrains
                        .plus(spaceTrain.bound to SelectedSpaceTrain(spaceTrain.number, fareId))
        return Selection(newSelectedSpaceTrains)
    }
}



