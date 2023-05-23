package com.beyondxscratch.mandaloreexpress.domain.booking.tax

import com.beyondxscratch.mandaloreexpress.domain.sharedkernel.Amount
import com.beyondxscratch.mandaloreexpress.domain.sharedkernel.Currency

data class TaxPortion(val amount: Amount, val currency: Currency)
