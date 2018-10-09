package br.com.bcunha.heavygear.model.api.alphavantage

import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParseException

import java.lang.reflect.Type

import br.com.bcunha.heavygear.model.pojo.alphavantage.TimeMomentAcao
import br.com.bcunha.heavygear.model.pojo.alphavantage.MetaDataAcao
import br.com.bcunha.heavygear.model.pojo.alphavantage.RespostaAcao
import br.com.bcunha.heavygear.model.pojo.alphavantage.TimeSeries

/**
 * Created by bruno on 11/01/18.
 */

class RespostaAcaoDeserializer : JsonDeserializer<RespostaAcao> {
    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): RespostaAcao {
        val gson = Gson()
        val respostaAcao = RespostaAcao()

        val jsonObject = json.asJsonObject

        val metaDataAcao = gson.fromJson<MetaDataAcao>(jsonObject.get("Meta Data"), MetaDataAcao::class.java)
        respostaAcao.metaDataAcao = metaDataAcao
        val timeMomentAcao = TimeMomentAcao()

        timeMomentAcao.Open = jsonObject.getAsJsonObject("Time Series (Daily)").getAsJsonObject(metaDataAcao.LastRefreshed).get("1. open").asString
        timeMomentAcao.High = jsonObject.getAsJsonObject("Time Series (Daily)").getAsJsonObject(metaDataAcao.LastRefreshed).get("2. high").asString
        timeMomentAcao.Low = jsonObject.getAsJsonObject("Time Series (Daily)").getAsJsonObject(metaDataAcao.LastRefreshed).get("3. low").asString
        timeMomentAcao.Close = jsonObject.getAsJsonObject("Time Series (Daily)").getAsJsonObject(metaDataAcao.LastRefreshed).get("4. close").asString
        timeMomentAcao.Volume = jsonObject.getAsJsonObject("Time Series (Daily)").getAsJsonObject(metaDataAcao.LastRefreshed).get("5. volume").asString

        val timeSeries = TimeSeries()
        timeSeries.timeMomentAcao = timeMomentAcao

        respostaAcao.timeSeries = timeSeries

        return respostaAcao
    }
}
