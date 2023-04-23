package com.beyondxscratch.columbiadexpress.domain.search.selection

import com.beyondxscratch.columbiadexpress.domain.sharedkernel.Price
import java.util.UUID

data class SelectedSpaceTrain(val spaceTrainNumber: String, val fareId: UUID, val price: Price)