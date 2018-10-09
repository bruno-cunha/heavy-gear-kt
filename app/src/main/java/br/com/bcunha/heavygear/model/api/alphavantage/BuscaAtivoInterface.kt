package br.com.bcunha.heavygear.model.api.alphavantage

import br.com.bcunha.heavygear.model.pojo.alphavantage.RespostaAcao
import br.com.bcunha.heavygear.model.pojo.alphavantage.RespostaMoeda
import retrofit2.Call
import retrofit2.http.GET

/**
 * Created by bruno on 09/01/18.
 */

interface BuscaAtivoInterface {
    @GET("query")
    fun getStock(@retrofit2.http.Query("function") function: String,
                 @retrofit2.http.Query("symbol") symbol: String,
                 @retrofit2.http.Query("interval") interval: String,
                 @retrofit2.http.Query("apikey") apiKey: String): Call<RespostaAcao>

    @GET("query")
    fun getCurrency(@retrofit2.http.Query("function") function: String,
                    @retrofit2.http.Query("symbol") symbol: String,
                    @retrofit2.http.Query("market") market: String,
                    @retrofit2.http.Query("apikey") apiKey: String): Call<RespostaMoeda>

}
