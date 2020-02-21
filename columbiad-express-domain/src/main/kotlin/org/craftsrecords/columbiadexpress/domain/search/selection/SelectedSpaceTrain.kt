package org.craftsrecords.columbiadexpress.domain.search.selection

import org.craftsrecords.columbiadexpress.domain.sharedkernel.Price
import java.util.UUID

data class SelectedSpaceTrain(val spaceTrainNumber: String, val fareId: UUID, val price: Price)