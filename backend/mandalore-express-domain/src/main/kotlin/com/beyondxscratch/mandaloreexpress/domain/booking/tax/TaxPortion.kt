package com.beyondxscratch.mandaloreexpress.domain.booking.tax

import com.beyondxscratch.mandaloreexpress.domain.sharedkernel.fare.Amount
import com.beyondxscratch.mandaloreexpress.domain.sharedkernel.fare.Currency

data class TaxPortion(val amount: Amount, val currency: Currency)
