package com.beyondxscratch.mandaloreexpress.domain.search.selection

import com.beyondxscratch.TypedParameterResolver
import com.beyondxscratch.mandaloreexpress.domain.sharedkernel.Random

class SelectedSpaceTrainParameterResolver : TypedParameterResolver<SelectedSpaceTrain>({ parameterContext, _ ->
    when {
        parameterContext.isAnnotated(Random::class.java) -> randomSelectedSpaceTrain()
        else -> selectedSpaceTrain()
    }
})