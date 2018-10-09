package br.com.bcunha.heavygear.model.pojo.alphavantage

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class MetaDataAcao (
    @SerializedName("1. Information")
    @Expose
    var Information:String? = null,
    @SerializedName("2. Symbol")
    @Expose
    var Symbol:String? = null,
    @SerializedName("3. Last Refreshed")
    @Expose
    var LastRefreshed:String? = null,
    @SerializedName("4. Interval")
    @Expose
    var Interval:String? = null,
    @SerializedName("5. Output Size")
    @Expose
    var OutputSize:String? = null,
    @SerializedName("6. Time Zone")
    @Expose
    var TimeZone:String? = null)

