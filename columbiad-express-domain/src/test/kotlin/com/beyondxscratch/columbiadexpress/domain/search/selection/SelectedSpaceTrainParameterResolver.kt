package com.beyondxscratch.columbiadexpress.domain.search.selection

import com.beyondxscratch.TypedParameterResolver
import com.beyondxscratch.columbiadexpress.domain.Random

class SelectedSpaceTrainParameterResolver : TypedParameterResolver<SelectedSpaceTrain>({ parameterContext, _ ->
    when {
        parameterContext.isAnnotated(Random::class.java) -> randomSelectedSpaceTrain()
        else -> selectedSpaceTrain()
    }
})