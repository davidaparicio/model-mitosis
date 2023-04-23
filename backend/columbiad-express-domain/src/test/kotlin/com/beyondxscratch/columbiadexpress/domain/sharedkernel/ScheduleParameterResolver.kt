package com.beyondxscratch.columbiadexpress.domain.sharedkernel

import com.beyondxscratch.TypedParameterResolver
import com.beyondxscratch.columbiadexpress.domain.Random

class ScheduleParameterResolver : TypedParameterResolver<Schedule>({ parameterContext, _ ->
    when {
        parameterContext.isAnnotated(Random::class.java) -> {
            randomSchedule()
        }
        else -> schedule()
    }
})
