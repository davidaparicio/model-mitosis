package com.beyondxscratch.mandaloreexpress.domain.search.selection

import com.beyondxscratch.mandaloreexpress.domain.sharedkernel.Price
import java.util.UUID

data class SelectedSpaceTrain(val spaceTrainNumber: String, val fareId: UUID, val price: Price)