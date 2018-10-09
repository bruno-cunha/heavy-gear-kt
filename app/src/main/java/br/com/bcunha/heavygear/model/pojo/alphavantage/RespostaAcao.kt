package br.com.bcunha.heavygear.model.pojo.alphavantage

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RespostaAcao(
    @SerializedName("Meta Data")
    @Expose
    var metaDataAcao: MetaDataAcao? = null,
    @SerializedName("Time Series (Daily)")
    @Expose
    var timeSeries: TimeSeries? = null)
