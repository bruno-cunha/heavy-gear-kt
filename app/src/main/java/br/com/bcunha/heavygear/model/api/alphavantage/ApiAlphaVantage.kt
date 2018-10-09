package br.com.bcunha.heavygear.model.api.alphavantage

import com.google.gson.Gson
import com.google.gson.GsonBuilder

import java.util.concurrent.TimeUnit

import br.com.bcunha.heavygear.model.pojo.alphavantage.RespostaAcao
import br.com.bcunha.heavygear.model.pojo.alphavantage.RespostaMoeda
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by bruno on 09/01/18.
 */

object ApiAlphaVantage {
    val BASE_URL_AV = "http://www.alphavantage.co/"
    val TIME_SERIES_INTRADAY = "TIME_SERIES_INTRADAY"
    val TIME_SERIES_DAILY = "TIME_SERIES_DAILY"
    val TIME_SERIES_MONTHLY = "TIME_SERIES_MONTHLY"
    val DIGITAL_CURRENCY_INTRADAY = "DIGITAL_CURRENCY_INTRADAY"
    val INTERVAL = "1min"
    val MARKET = "BRL"

    private var retrofit: Retrofit = getRetrofit()

    fun getRetrofit(): Retrofit {
        val gsonBuilder = GsonBuilder()
        gsonBuilder.registerTypeAdapter(RespostaAcao::class.java, RespostaAcaoDeserializer())
        gsonBuilder.registerTypeAdapter(RespostaMoeda::class.java, RespostaMoedaDeserializer())
        val gson = gsonBuilder.create()

        val client = OkHttpClient.Builder()
                .readTimeout(30, TimeUnit.SECONDS)
                .build()

        retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL_AV)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
        return retrofit
    }

}
