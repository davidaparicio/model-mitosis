package org.craftsrecords.columbiadexpress.domain.search.selection

import org.craftsrecords.TypedParameterResolver
import org.craftsrecords.columbiadexpress.domain.Random

class SelectedSpaceTrainParameterResolver : TypedParameterResolver<SelectedSpaceTrain>({ parameterContext, _ ->
    when {
        parameterContext.isAnnotated(Random::class.java) -> randomSelectedSpaceTrain()
        else -> selectedSpaceTrain()
    }
})