package com.beyondxscratch.columbiadexpress.infrastructure.configurations

import com.beyondxscratch.columbiadexpress.domain.ColumbiadExpress
import com.beyondxscratch.columbiadexpress.domain.api.DomainService
import com.beyondxscratch.columbiadexpress.domain.stubs.Stub
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.ComponentScan.Filter
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.FilterType.ANNOTATION

@Configuration
@ComponentScan(
        basePackageClasses = [ColumbiadExpress::class],
        includeFilters = [Filter(type = ANNOTATION, value = [DomainService::class, Stub::class])])
class DomainConfiguration