package com.beyondxscratch.mandaloreexpress.domain.selection

import com.beyondxscratch.mandaloreexpress.domain.spacetrain.fare.Price
import java.util.UUID

data class SelectedSpaceTrain(val spaceTrainNumber: String, val fareId: UUID, val price: Price)