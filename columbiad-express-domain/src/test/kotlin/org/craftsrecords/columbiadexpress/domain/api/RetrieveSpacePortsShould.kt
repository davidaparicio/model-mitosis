package org.craftsrecords.columbiadexpress.domain.api

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

interface RetrieveSpacePortsShould {

    val retrieveSpacePorts: RetrieveSpacePorts

    @Test
    fun `find all space ports having a given substring in their name`() {
        val result = retrieveSpacePorts `having in their name` "Cosmo"
        assertThat(result).allMatch { it.name.contains("Cosmo") }
    }

}