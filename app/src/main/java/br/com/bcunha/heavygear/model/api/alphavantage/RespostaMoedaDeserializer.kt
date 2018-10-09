package br.com.bcunha.heavygear.model.api.alphavantage

import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParseException

import java.lang.reflect.Type

import br.com.bcunha.heavygear.model.pojo.alphavantage.MetaDataMoeda
import br.com.bcunha.heavygear.model.pojo.alphavantage.RespostaMoeda
import br.com.bcunha.heavygear.model.pojo.alphavantage.TimeMomentMoeda
import br.com.bcunha.heavygear.model.pojo.alphavantage.TimeSeries

/**
 * Created by bruno on 11/01/18.
 */

class RespostaMoedaDeserializer : JsonDeserializer<RespostaMoeda> {
    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): RespostaMoeda {
        val gson = Gson()
        val respostaMoeda = RespostaMoeda()

        val jsonObject = json.asJsonObject

        val metaDataMoeda = gson.fromJson<MetaDataMoeda>(jsonObject.get("Meta Data"), MetaDataMoeda::class.java)
        respostaMoeda.setMetaDataMoeda(metaDataMoeda)
        val timeMomentMoeda = TimeMomentMoeda()

        timeMomentMoeda.PriceBRL = jsonObject.getAsJsonObject("Time Series (Digital Currency Intraday)").getAsJsonObject(metaDataMoeda.LastRefreshed).get("1a. price (BRL)").asString
        timeMomentMoeda.PriceUSD = jsonObject.getAsJsonObject("Time Series (Digital Currency Intraday)").getAsJsonObject(metaDataMoeda.LastRefreshed).get("1b. price (USD)").asString
        timeMomentMoeda.Volume = jsonObject.getAsJsonObject("Time Series (Digital Currency Intraday)").getAsJsonObject(metaDataMoeda.LastRefreshed).get("2. volume").asString
        timeMomentMoeda.MarketCapUSD = jsonObject.getAsJsonObject("Time Series (Digital Currency Intraday)").getAsJsonObject(metaDataMoeda.LastRefreshed).get("3. market cap (USD)").asString

        val timeSeries = TimeSeries()
        timeSeries.timeMomentMoeda = timeMomentMoeda

        respostaMoeda.timeSeries = timeSeries

        return respostaMoeda
    }
}
