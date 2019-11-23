package org.craftsrecords.columbiadexpress.domain.search

import org.craftsrecords.columbiadexpress.domain.Random
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.ParameterContext
import org.junit.jupiter.api.extension.support.TypeBasedParameterResolver

class SearchParameterResolver : TypeBasedParameterResolver<Search>() {
    override fun resolveParameter(parameterContext: ParameterContext, extensionContext: ExtensionContext): Search {
        if (parameterContext.isAnnotated(Random::class.java)) {
            return randomSearch()
        }
        return search()
    }
}