package com.beyondxscratch.mandaloreexpress.infrastructure.configurations

import com.beyondxscratch.mandaloreexpress.domain.MandaloreExpress
import com.beyondxscratch.mandaloreexpress.domain.api.DomainService
import com.beyondxscratch.mandaloreexpress.domain.stubs.Stub
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.ComponentScan.Filter
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.FilterType.ANNOTATION

@Configuration
@ComponentScan(
        basePackageClasses = [MandaloreExpress::class],
        includeFilters = [Filter(type = ANNOTATION, value = [DomainService::class, Stub::class])])
class DomainConfiguration