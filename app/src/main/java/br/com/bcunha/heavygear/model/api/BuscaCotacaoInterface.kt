package br.com.bcunha.heavygear.model.api

import br.com.bcunha.heavygear.model.pojo.RespostaQuote
import retrofit2.Call
import retrofit2.http.GET

/**
 * Created by BRUNO on 09/10/2016.
 */

interface BuscaCotacaoInterface {

    @GET("yql")
    fun getQuotes(@retrofit2.http.Query("q") query: String, @retrofit2.http.Query("env") env: String, @retrofit2.http.Query("format") format: String): Call<RespostaQuote>
}
