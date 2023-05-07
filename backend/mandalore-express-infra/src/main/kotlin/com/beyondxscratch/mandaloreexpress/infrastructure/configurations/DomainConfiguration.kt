package com.beyondxscratch.mandaloreexpress.infrastructure.configurations

import com.beyondxscratch.mandaloreexpress.annotations.AntiCorruptionLayer
import com.beyondxscratch.mandaloreexpress.annotations.DomainService
import com.beyondxscratch.mandaloreexpress.annotations.Stub
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.ComponentScan.Filter
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.FilterType.ANNOTATION

@Configuration
@ComponentScan(
        basePackages = ["com.beyondxscratch.mandaloreexpress.domain"],
        includeFilters = [Filter(type = ANNOTATION, value = [DomainService::class, Stub::class, AntiCorruptionLayer::class])]
)
class DomainConfiguration