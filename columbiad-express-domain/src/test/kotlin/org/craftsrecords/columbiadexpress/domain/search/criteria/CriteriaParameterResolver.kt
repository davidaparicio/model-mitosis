package org.craftsrecords.columbiadexpress.domain.search.criteria

import org.craftsrecords.columbiadexpress.domain.Random
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.ParameterContext
import org.junit.jupiter.api.extension.support.TypeBasedParameterResolver

class CriteriaParameterResolver : TypeBasedParameterResolver<Criteria>() {
    override fun resolveParameter(parameterContext: ParameterContext, extensionContext: ExtensionContext): Criteria {
        if (parameterContext.isAnnotated(Random::class.java)) {
            return randomCriteria()
        }
        return criteria()
    }
}