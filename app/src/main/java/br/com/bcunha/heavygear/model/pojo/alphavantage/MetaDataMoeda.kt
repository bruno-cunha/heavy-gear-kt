package br.com.bcunha.heavygear.model.pojo.alphavantage

/**
 * Created by Bruno on 17/01/2018.
 */
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class MetaDataMoeda (
    @SerializedName("1. Information")
    @Expose
    var Information:String? = null,
    @SerializedName("2. Digital Currency Code")
    @Expose
    var DigitalCurrencyCode:String? = null,
    @SerializedName("3. Digital Currency Name")
    @Expose
    var DigitalCurrencyName:String? = null,
    @SerializedName("4. Market Code")
    @Expose
    var MarketCode:String? = null,
    @SerializedName("5. Market Name")
    @Expose
    var MarketName:String? = null,
    @SerializedName("6. Interval")
    @Expose
    var Interval:String? = null,
    @SerializedName("7. Last Refreshed")
    @Expose
    var LastRefreshed:String? = null,
    @SerializedName("8. Time Zone")
    @Expose
    var TimeZone:String? = null)
