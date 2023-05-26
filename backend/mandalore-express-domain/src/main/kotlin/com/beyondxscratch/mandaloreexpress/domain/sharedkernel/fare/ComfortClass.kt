package com.beyondxscratch.mandaloreexpress.domain.sharedkernel.fare

enum class ComfortClass//(val taxRate: TaxRate)
{
    FIRST//(TaxRate("0.2".toBigDecimal()))
    ,
    SECOND//(TaxRate("0.1".toBigDecimal()))
    ,
}