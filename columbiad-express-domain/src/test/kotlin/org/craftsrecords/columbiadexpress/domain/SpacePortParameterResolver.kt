package org.craftsrecords.columbiadexpress.domain

import org.craftsrecords.columbiadexpress.domain.spaceport.AstronomicalBody.EARTH
import org.craftsrecords.columbiadexpress.domain.spaceport.AstronomicalBody.values
import org.craftsrecords.columbiadexpress.domain.spaceport.SpacePort
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.ParameterContext
import org.junit.jupiter.api.extension.ParameterResolver
import java.util.*

class SpacePortParameterResolver : ParameterResolver {
    override fun supportsParameter(parameterContext: ParameterContext, extensionContext: ExtensionContext): Boolean {
        return parameterContext.parameter.type == SpacePort::class.java
    }

    override fun resolveParameter(parameterContext: ParameterContext, extensionContext: ExtensionContext): Any {
        if (parameterContext.isAnnotated(Random::class.java)) {
            return randomSpacePort()
        }
        return spacePort()
    }

    private fun spacePort() = SpacePort(id = UUID.fromString("fa9f2371-5b13-40a1-bd18-42db3371f073"), name = "name", location = EARTH)
    private fun randomSpacePort() = SpacePort(name = UUID.randomUUID().toString(), location = values().random())

}
