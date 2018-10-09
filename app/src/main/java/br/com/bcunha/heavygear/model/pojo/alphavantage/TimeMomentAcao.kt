package br.com.bcunha.heavygear.model.pojo.alphavantage

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class TimeMomentAcao(
    @SerializedName("1. open")
    @Expose
    var Open:String? = null,
    @SerializedName("2. high")
    @Expose
    var High:String? = null,
    @SerializedName("3. low")
    @Expose
    var Low:String? = null,
    @SerializedName("4. close")
    @Expose
    var Close:String? = null,
    @SerializedName("5. volume")
    @Expose
    var Volume:String? = null)
