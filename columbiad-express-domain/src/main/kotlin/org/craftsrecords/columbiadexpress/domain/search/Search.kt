package org.craftsrecords.columbiadexpress.domain.search

import org.craftsrecords.columbiadexpress.domain.search.criteria.Criteria
import java.util.UUID
import java.util.UUID.randomUUID

data class Search(val id: UUID = randomUUID(), val criteria: Criteria, val spaceTrains: List<SpaceTrain>) {
    init {
        require(spaceTrains.isNotEmpty()) {
            "list of space trains cannot be empty"
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Search

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }


}