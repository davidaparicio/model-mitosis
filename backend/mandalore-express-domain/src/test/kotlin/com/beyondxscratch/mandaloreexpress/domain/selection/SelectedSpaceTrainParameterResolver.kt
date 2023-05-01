package com.beyondxscratch.mandaloreexpress.domain.selection

import com.beyondxscratch.TypedParameterResolver
import com.beyondxscratch.mandaloreexpress.domain.Random

class SelectedSpaceTrainParameterResolver : TypedParameterResolver<SelectedSpaceTrain>({ parameterContext, _ ->
    when {
        parameterContext.isAnnotated(Random::class.java) -> randomSelectedSpaceTrain()
        else -> selectedSpaceTrain()
    }
})