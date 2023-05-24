package com.beyondxscratch.mandaloreexpress.domain.selection

import com.beyondxscratch.TypedParameterResolver
import com.beyondxscratch.mandaloreexpress.domain.Random
import com.beyondxscratch.mandaloreexpress.domain.search.selection.Selection

class SelectionParameterResolver : TypedParameterResolver<Selection>({ parameterContext, _ ->
    when {
        parameterContext.isAnnotated(Random::class.java) -> randomSelection()
        else -> selection()
    }
})