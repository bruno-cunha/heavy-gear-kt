package br.com.bcunha.heavygear.model.pojo.alphavantage

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class TimeMomentMoeda(
    @SerializedName("1a. price (BRL)")
    @Expose
    var PriceBRL:String? = null,
    @SerializedName("1b. price (USD)")
    @Expose
    var PriceUSD:String? = null,
    @SerializedName("2. volume")
    @Expose
    var Volume:String? = null,
    @SerializedName("3. market cap (USD)")
    @Expose
    var MarketCapUSD:String? = null)

