package com.beyondxscratch.mandaloreexpress.domain.booking.tax

import com.beyondxscratch.mandaloreexpress.domain.money.Amount
import com.beyondxscratch.mandaloreexpress.domain.money.Currency

data class TaxPortion(val amount: Amount, val currency: Currency)
