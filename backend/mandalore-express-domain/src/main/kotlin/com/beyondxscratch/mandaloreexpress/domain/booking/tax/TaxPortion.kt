package com.beyondxscratch.mandaloreexpress.domain.booking.tax

import com.beyondxscratch.mandaloreexpress.domain.sharedkernel.fare.Amount
import com.beyondxscratch.mandaloreexpress.domain.sharedkernel.fare.Currency

data class TaxPortion(val amount: Amount, val currency: Currency){
    operator fun plus(anotherTaxPortion: TaxPortion): TaxPortion {
        require(currency == anotherTaxPortion.currency) {
            "Cannot sum tax portions with different currencies"
        }
        return TaxPortion(amount + anotherTaxPortion.amount, currency)
    }
}
