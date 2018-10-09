package br.com.bcunha.heavygear.model.pojo

import com.google.gson.annotations.SerializedName

/**
 * Created by bruno on 18/04/17.
 */

class Quote {
    @SerializedName("symbol")
    fun getsymbol(): String {
        return this.symbol
    }

    fun setsymbol(symbol: String) {
        this.symbol = symbol
    }

    internal var symbol: String = ""

    @get:SerializedName("AverageDailyVolume")
    var averageDailyVolume: String = ""

    @get:SerializedName("Change")
    var change: String = ""

    @get:SerializedName("DaysLow")
    var daysLow: String = ""

    @get:SerializedName("DaysHigh")
    var daysHigh: String = ""

    @get:SerializedName("YearLow")
    var yearLow: String = ""

    @get:SerializedName("YearHigh")
    var yearHigh: String = ""

    @get:SerializedName("MarketCapitalization")
    var marketCapitalization: String = ""

    @get:SerializedName("LastTradePriceOnly")
    var lastTradePriceOnly: String = ""

    @get:SerializedName("DaysRange")
    var daysRange: String = ""

    @get:SerializedName("Name")
    var name: String = ""

    @SerializedName("Symbol")
    fun getSymbol(): String {
        return this.SymbolUP.substring(0, 5)
    }

    fun setSymbol(symbol: String) {
        this.SymbolUP = symbol
    }

    internal var SymbolUP: String = ""

    @get:SerializedName("Volume")
    var volume: String = ""

    @get:SerializedName("StockExchange")
    var stockExchange: String = ""
}