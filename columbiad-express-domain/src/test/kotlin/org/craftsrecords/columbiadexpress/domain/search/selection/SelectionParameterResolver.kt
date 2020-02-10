package org.craftsrecords.columbiadexpress.domain.search.selection

import org.craftsrecords.TypedParameterResolver
import org.craftsrecords.columbiadexpress.domain.Random

class SelectionParameterResolver : TypedParameterResolver<Selection>({ parameterContext, _ ->
    when {
        parameterContext.isAnnotated(Random::class.java) -> randomSelection()
        else -> selection()
    }
})