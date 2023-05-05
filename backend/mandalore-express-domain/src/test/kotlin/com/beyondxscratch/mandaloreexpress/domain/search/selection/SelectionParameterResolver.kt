package com.beyondxscratch.mandaloreexpress.domain.search.selection

import com.beyondxscratch.TypedParameterResolver
import com.beyondxscratch.mandaloreexpress.domain.annotations.Random

class SelectionParameterResolver : TypedParameterResolver<Selection>({ parameterContext, _ ->
    when {
        parameterContext.isAnnotated(Random::class.java) -> randomSelection()
        else -> selection()
    }
})