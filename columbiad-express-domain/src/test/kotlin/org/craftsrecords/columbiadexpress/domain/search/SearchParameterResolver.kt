package org.craftsrecords.columbiadexpress.domain.search

import org.craftsrecords.TypedParameterResolver
import org.craftsrecords.columbiadexpress.domain.Random

class SearchParameterResolver : TypedParameterResolver<Search>({ parameterContext, _ ->
    when {
        parameterContext.isAnnotated(Random::class.java) -> {
            randomSearch()
        }

        else -> search()
    }
})
