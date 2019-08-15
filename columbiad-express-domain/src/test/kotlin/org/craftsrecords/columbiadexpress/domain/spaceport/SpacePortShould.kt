package org.craftsrecords.columbiadexpress.domain.spaceport

import org.assertj.core.api.Assertions.assertThat
import org.craftsrecords.columbiadexpress.domain.EqualityShould
import org.craftsrecords.columbiadexpress.domain.InjectDomainObjects
import org.craftsrecords.columbiadexpress.domain.spaceport.AstronomicalBody.EARTH
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@InjectDomainObjects
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

    @Test
    fun `recognize if it is on the same planet than another SpacePort`(@OnEarth anotherSpacePort: SpacePort) {
        val areNotOnTheSamePlanet = spacePort `is not on the same planet than` anotherSpacePort
        assertThat(areNotOnTheSamePlanet).isFalse()
    }

    @Test
    fun `recognize if it is not on the same planet than another SpacePort`(@OnMoon anotherSpacePort: SpacePort) {
        val areNotOnTheSamePlanet = spacePort `is not on the same planet than` anotherSpacePort
        assertThat(areNotOnTheSamePlanet).isTrue()
    }
}