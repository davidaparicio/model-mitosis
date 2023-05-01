package com.beyondxscratch.mandaloreexpress.domain

import com.beyondxscratch.TypedParameterResolver

class ScheduleParameterResolver : TypedParameterResolver<Schedule>({ parameterContext, _ ->
    when {
        parameterContext.isAnnotated(Random::class.java) -> {
            randomSchedule()
        }

        else -> schedule()
    }
})
