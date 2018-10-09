package br.com.bcunha.heavygear.model.api

import com.google.gson.Gson
import com.google.gson.GsonBuilder

import br.com.bcunha.heavygear.model.pojo.Ativo
import br.com.bcunha.heavygear.model.pojo.RespostaQuote
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by BRUNO on 09/10/2016.
 */
object ApiClient {
    val BASE_URL = "http://query.yahooapis.com/v1/public/"
    val QUERY_QUOTE = "select * from yahoo.finance.quote where symbol in (?codigo?)"
    val QUERY_QUOTES = "select * from yahoo.finance.quotes where symbol in (\"?codigo?\")"
    val ENV = "store://datatables.org/alltableswithkeys"
    val FORMAT = "json"

    private var retrofit: Retrofit = getRetrofit()

    fun getRetrofit(): Retrofit {
            val gsonBuilder = GsonBuilder()
            gsonBuilder.registerTypeAdapter(RespostaQuote::class.java, RespostaQuoteDeserializer())
            val gson = gsonBuilder.create()

            retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build()
        return retrofit
    }

    fun formatCodigo(acoes: List<Ativo>): String {
        if (acoes.isEmpty()) {
            return "\"\""
        }

        val codigos = StringBuffer()
        var primeiro = true
        for (ativo in acoes) {
            if (primeiro) {
                codigos.append("\"").append(ativo.codigo.toString()).append(".SA").append("\"")
                primeiro = false
            } else {
                codigos.append(",").append("\"").append(ativo.codigo.toString()).append(".SA").append("\"")
            }
        }
        return codigos.toString()
    }
}
