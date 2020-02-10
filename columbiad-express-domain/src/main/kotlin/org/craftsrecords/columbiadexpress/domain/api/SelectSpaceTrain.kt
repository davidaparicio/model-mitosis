package org.craftsrecords.columbiadexpress.domain.api

import org.craftsrecords.columbiadexpress.domain.search.Search
import java.util.UUID

interface SelectSpaceTrain {
    infix fun `having the number`(spaceTrainNumber: String): SelectFare = selectSpaceTrain(spaceTrainNumber)
    fun selectFareOfSpaceTrainInSearch(spaceTrainNumber: String, fareId: UUID, searchId: UUID): Search

    private fun selectSpaceTrain(number: String): SelectFare =
            { fareId: UUID -> this.selectFare(number, fareId) }

    private fun selectFare(number: String, fareId: UUID): InSearchSelection =
            { searchId: UUID -> this.selectFareOfSpaceTrainInSearch(number, fareId, searchId) }
}

typealias SelectFare = (UUID) -> InSearchSelection

infix fun SelectFare.`with the fare`(fareId: UUID): InSearchSelection = this.invoke(fareId)

typealias InSearchSelection = (UUID) -> Search

infix fun InSearchSelection.`in search`(searchId: UUID): Search = this.invoke(searchId)
