package br.com.bcunha.heavygear.model.pojo.alphavantage

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RespostaMoeda {

    @SerializedName("Meta Data")
    @Expose
    var metaDataAcao: MetaDataMoeda? = null
        private set
    @SerializedName("Time Series (Digital Currency Intraday)")
    @Expose
    var timeSeries: TimeSeries? = null

    fun setMetaDataMoeda(metaDataMoeda: MetaDataMoeda) {
        this.metaDataAcao = metaDataMoeda
    }

}
