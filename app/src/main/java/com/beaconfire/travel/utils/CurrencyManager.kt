package com.beaconfire.travel.utils

import com.beaconfire.travel.repo.model.Destination

class CurrencyManager private constructor() {

    val currencyOptions = listOf("USD", "CNY", "JPY")
    var currency: String = "USD"
        private set

    fun setCurrency(newCurrency: String){
        this.currency = newCurrency
    }

    fun clear(){
        currency = "USD"
    }

    fun getPriceInSelectedCurrency(price: Double): Double{
        if (currency == "CNY") return price * 7.1
        if (currency == "JPY") return price * 146.4
        return price
    }

    companion object {
        @Volatile
        private var INSTANCE: CurrencyManager? = null

        fun getInstance(): CurrencyManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: CurrencyManager().also {
                    INSTANCE = it
                }
            }
        }
    }
}