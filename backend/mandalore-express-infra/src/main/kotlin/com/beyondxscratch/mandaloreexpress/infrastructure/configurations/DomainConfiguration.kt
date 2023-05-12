package com.beyondxscratch.mandaloreexpress.infrastructure.configurations

import com.beyondxscratch.mandaloreexpress.domain.search.SpaceTrainsFinder
import com.beyondxscratch.mandaloreexpress.annotations.DomainService
import com.beyondxscratch.mandaloreexpress.annotations.Stub
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.ComponentScan.Filter
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.FilterType.ANNOTATION

@Configuration
@ComponentScan(
        basePackageClasses = [SpaceTrainsFinder::class],
        includeFilters = [Filter(type = ANNOTATION, value = [DomainService::class, Stub::class])]
)
class DomainConfiguration