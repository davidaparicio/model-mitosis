package com.beyondxscratch.mandaloreexpress.domain.booking.spi

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import java.util.*
import java.util.UUID.randomUUID

interface IsSelectionCompleteShould {
    val completeSelectionSearchId : UUID
    val incompleteSelectionSearchId : UUID
    val isSelectionComplete : IsSelectionComplete

    @Test
    fun `return true when the search's selection is complete`(){
       assertThat(isSelectionComplete of completeSelectionSearchId).isTrue
    }

    @Test
    fun `return false when the search's selection is incomplete`(){
       assertThat(isSelectionComplete of incompleteSelectionSearchId).isFalse
    }

    @Test
    fun `throw a NoSuchElementException when there is no search`(){
        val unknownSearchId = randomUUID()

        assertThatThrownBy { isSelectionComplete of unknownSearchId }
            .isInstanceOf(NoSuchElementException::class.java)
            .hasMessage("Unknown search $unknownSearchId")
    }
}