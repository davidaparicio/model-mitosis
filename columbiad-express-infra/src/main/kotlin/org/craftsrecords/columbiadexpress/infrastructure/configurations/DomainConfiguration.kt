package org.craftsrecords.columbiadexpress.infrastructure.configurations

import org.craftsrecords.columbiadexpress.domain.ColumbiadExpress
import org.craftsrecords.columbiadexpress.domain.spaceport.api.DomainService
import org.craftsrecords.columbiadexpress.domain.spaceport.stubs.Stub
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.ComponentScan.Filter
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.FilterType.ANNOTATION

@Configuration
@ComponentScan(
        basePackageClasses = [ColumbiadExpress::class],
        includeFilters = [Filter(type = ANNOTATION, value = [DomainService::class, Stub::class])])
class DomainConfiguration