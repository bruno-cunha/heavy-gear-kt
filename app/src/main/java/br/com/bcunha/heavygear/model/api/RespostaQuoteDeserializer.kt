package br.com.bcunha.heavygear.model.api

import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParseException

import java.lang.reflect.Type
import java.util.ArrayList

import br.com.bcunha.heavygear.model.pojo.Query
import br.com.bcunha.heavygear.model.pojo.Quote
import br.com.bcunha.heavygear.model.pojo.RespostaQuote
import br.com.bcunha.heavygear.model.pojo.Results

/**
 * Created by bruno on 17/04/17.
 */

class RespostaQuoteDeserializer : JsonDeserializer<RespostaQuote> {

    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): RespostaQuote {
        val gson = Gson()
        val respostaQuote: RespostaQuote

        val jsonObject = json.asJsonObject
        if (jsonObject.getAsJsonObject("query").get("count").asInt <= 1) {
            val jsonQuoteElement = jsonObject.getAsJsonObject("query").getAsJsonObject("results").get("quote")
            val quote = gson.fromJson<Quote>(jsonQuoteElement, Quote::class.java)

            val listaQuotes = ArrayList<Quote>()
            listaQuotes.add(quote)

            respostaQuote = RespostaQuote(Query(jsonObject.getAsJsonObject("query").get("count").asInt, Results(listaQuotes)))
        } else {
            respostaQuote = gson.fromJson<RespostaQuote>(json, RespostaQuote::class.java)
        }

        return respostaQuote
    }
}
