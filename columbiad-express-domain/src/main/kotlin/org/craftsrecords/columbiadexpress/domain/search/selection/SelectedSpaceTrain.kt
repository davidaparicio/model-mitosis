package org.craftsrecords.columbiadexpress.domain.search.selection

import org.craftsrecords.columbiadexpress.domain.search.Price
import java.util.*

data class SelectedSpaceTrain(val spaceTrainNumber: String, val fareId: UUID, val price: Price)