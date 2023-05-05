package com.beyondxscratch.mandaloreexpress.domain.search.spacetrain

import com.beyondxscratch.TypedParameterResolver
import com.beyondxscratch.mandaloreexpress.domain.annotations.Random

class ScheduleParameterResolver : TypedParameterResolver<Schedule>({ parameterContext, _ ->
    when {
        parameterContext.isAnnotated(Random::class.java) -> {
            randomSchedule()
        }
        else -> schedule()
    }
})
