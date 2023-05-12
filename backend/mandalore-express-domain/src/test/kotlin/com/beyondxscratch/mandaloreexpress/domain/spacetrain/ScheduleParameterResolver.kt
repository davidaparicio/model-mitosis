package com.beyondxscratch.mandaloreexpress.domain.spacetrain

import com.beyondxscratch.TypedParameterResolver
import com.beyondxscratch.mandaloreexpress.domain.Random
import com.beyondxscratch.mandaloreexpress.domain.sharedkernel.Schedule

class ScheduleParameterResolver : TypedParameterResolver<Schedule>({ parameterContext, _ ->
    when {
        parameterContext.isAnnotated(Random::class.java) -> {
            randomSchedule()
        }
        else -> schedule()
    }
})
