package org.craftsrecords.columbiadexpress.domain.api

import org.assertj.core.api.Assertions.assertThat
import org.craftsrecords.columbiadexpress.domain.spaceport.api.RetrieveSpacePorts
import org.junit.jupiter.api.Test

interface RetrieveSpacePortsShould {

    val retrieveSpacePorts: RetrieveSpacePorts

    @Test
    fun `find all space ports having their name containing a partial name`() {
        val result = retrieveSpacePorts `having their name containing` "Cosmo"
        assertThat(result).allMatch { it.name.contains("Cosmo") }
    }

}