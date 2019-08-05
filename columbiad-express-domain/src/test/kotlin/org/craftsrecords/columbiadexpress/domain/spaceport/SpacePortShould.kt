package org.craftsrecords.columbiadexpress.domain.spaceport

import org.assertj.core.api.Assertions.assertThat
import org.craftsrecords.columbiadexpress.domain.EqualityShould
import org.craftsrecords.columbiadexpress.domain.spaceport.AstronomicalBody.EARTH
import org.craftsrecords.columbiadexpress.domain.spaceport.AstronomicalBody.MOON
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.util.*

@DisplayName("SpacePort should")
class SpacePortShould : EqualityShould<SpacePort> {

    private val spacePort = SpacePort(name = "Cape Canaveral Air Force Station", location = EARTH)

    @Test
    fun `recognize sub sequences in its name`() {
        val hasSubSequenceInName = spacePort `has a name containing` "Air Force"
        assertThat(hasSubSequenceInName).isTrue()
    }

    @Test
    fun `recognize sub sequences in its name ignoring the case`() {
        val hasSubSequenceInName = spacePort `has a name containing` "air force"
        assertThat(hasSubSequenceInName).isTrue()
    }

    @Test
    fun `not recognize missing sub sequences from its name`() {
        val hasSubSequenceInName = spacePort `has a name containing` "Cosmodrome"
        assertThat(hasSubSequenceInName).isFalse()
    }

    override fun createValue() = SpacePort(id = UUID.fromString("fa9f2371-5b13-40a1-bd18-42db3371f073"), name = "name", location = EARTH)
    override fun createAnotherValue() = SpacePort(id = UUID.fromString("9229d410-c1ff-4abe-b47a-328cbd4a78bf"), name = "other", location = MOON)
}