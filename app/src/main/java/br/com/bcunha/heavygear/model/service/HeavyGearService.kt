package br.com.bcunha.heavygear.model.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.Handler
import android.os.IBinder
import android.os.Parcelable
import android.preference.PreferenceManager
import android.support.v4.content.LocalBroadcastManager
import android.util.Log
import android.widget.Toast

import java.util.ArrayList
import java.util.Objects

import br.com.bcunha.heavygear.model.api.alphavantage.ApiAlphaVantage
import br.com.bcunha.heavygear.model.api.alphavantage.ApiAlphaVantageKey
import br.com.bcunha.heavygear.model.api.alphavantage.BuscaAtivoInterface
import br.com.bcunha.heavygear.model.pojo.Ativo
import br.com.bcunha.heavygear.model.pojo.alphavantage.RespostaAcao
import br.com.bcunha.heavygear.model.pojo.alphavantage.RespostaMoeda
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HeavyGearService : Service() {
    private var frequenciaAtualizacao: Int = 0

    private var ativo = false
    private val mBinder = HeavyBinder()
    private var apiAlphaVantageClient: BuscaAtivoInterface? = null

    private var worker: Worker? = null
    private var handler = Handler()
    public var watchListService: MutableList<Ativo> = mutableListOf<Ativo>()

    override fun onBind(intent: Intent): IBinder? {
        Log.i(LOG_TAG, "onBind")

        // Buscar watchListService  do intent
        watchListService = intent.getParcelableArrayListExtra<Ativo>("watchListService").toMutableList()

        executar()
        ativo = true

        return mBinder
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        if (worker != null) {
            handler.postDelayed(worker, frequenciaAtualizacao.toLong())
        }

        // Buscar watchListService  do intent
        watchListService = ArrayList<Ativo>()

        Log.i(LOG_TAG, "onStartCommand")
        //return(START_STICKY);
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onCreate() {
        super.onCreate()

        apiAlphaVantageClient = ApiAlphaVantage.getRetrofit().create<BuscaAtivoInterface>(BuscaAtivoInterface::class.java)
        worker = Worker(this)
        atualizaTimer()

        Log.i(LOG_TAG, "onCreate")
    }

    override fun onDestroy() {
        Log.i(LOG_TAG, "onDestroy")
        stopSelf()
        super.onDestroy()
    }

    override fun onUnbind(intent: Intent): Boolean {
        Log.i(LOG_TAG, "onUnbind")
        ativo = false
        return false
    }

    fun atualizaTimer() {
        frequenciaAtualizacao = Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(this.applicationContext).getString(PREF_FREQUENCIA_ATUALIZACAO, "10000"))
    }

    private inner class Worker(var context: Context) : Runnable {

        override fun run() {
            if (!ativo) {
                return
            }
            if (watchListService.size >= 1) {
                for (ativo in watchListService) {
                    val index = watchListService.indexOf(ativo)

                    if (watchListService[index].getTipo() == "MOEDA") {
                        apiAlphaVantageClient!!.getCurrency(ApiAlphaVantage.DIGITAL_CURRENCY_INTRADAY,
                                ativo.codigo.toString(),
                                ApiAlphaVantage.MARKET,
                                ApiAlphaVantageKey.ApiKey)
                                .enqueue(object : Callback<RespostaMoeda> {
                                    override fun onResponse(call: Call<RespostaMoeda>, response: Response<RespostaMoeda>) {
                                        if (response.body() == null) {
                                            handler.post(worker)
                                            return
                                        }

                                        if (index <= watchListService.size - 1) {
                                            val cotacao = parseDouble(response.body().timeSeries?.timeMomentMoeda?.PriceBRL)
                                            val cotacaoDolar = parseDouble(response.body().timeSeries?.timeMomentMoeda?.PriceUSD)
                                            watchListService[index].setCotacao(cotacao)
                                            watchListService[index].cotaocaoDolar = cotacaoDolar
                                            val intent = Intent(ACTION_HEAVYSERVICE)
                                            intent.putExtra("ativo", watchListService[index])
                                            intent.putExtra("index", index)
                                            LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
                                        }
                                    }

                                    override fun onFailure(call: Call<RespostaMoeda>, t: Throwable) {
                                        //Toast.makeText(getApplicationContext(), "Serviço Sem Resposta", Toast.LENGTH_LONG).show();
                                    }
                                })
                    } else {
                        apiAlphaVantageClient!!.getStock(ApiAlphaVantage.TIME_SERIES_DAILY,
                                ativo.codigo + ".SA",
                                ApiAlphaVantage.INTERVAL,
                                ApiAlphaVantageKey.ApiKey)
                                .enqueue(object : Callback<RespostaAcao> {
                                    override fun onResponse(call: Call<RespostaAcao>, response: Response<RespostaAcao>) {
                                        if (response.body() == null) {
                                            handler.post(worker)
                                            return
                                        }

                                        if (index <= watchListService.size - 1) {
                                            val cotacao = parseDouble(response.body().timeSeries?.timeMomentAcao?.Close)
                                            watchListService[index].variacao = calculaVariacao(parseDouble(response.body().timeSeries?.timeMomentAcao?.Open), cotacao)
                                            watchListService[index].setCotacao(cotacao)
                                            watchListService[index].minimaDia = parseDouble(response.body().timeSeries?.timeMomentAcao?.Low)
                                            watchListService[index].maximaDia = parseDouble(response.body().timeSeries?.timeMomentAcao?.High)
                                            watchListService[index].abertura = parseDouble(response.body().timeSeries?.timeMomentAcao?.Open)
                                            watchListService[index].volumeNegociacao = parseInteger(response.body().timeSeries?.timeMomentAcao?.Volume)

                                            val intent = Intent(ACTION_HEAVYSERVICE)
                                            intent.putExtra("ativo", watchListService[index])
                                            intent.putExtra("index", index)
                                            LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
                                        }
                                    }

                                    override fun onFailure(call: Call<RespostaAcao>, t: Throwable) {
                                        //Toast.makeText(getApplicationContext(), "Serviço Sem Resposta", Toast.LENGTH_LONG).show();
                                    }
                                })
                    }
                }
            }

            //Log.i(LOG_TAG, "Consulta Executada");

            handler.postDelayed(this, frequenciaAtualizacao.toLong())
        }
    }

    fun executar() {
        if (worker != null) {
            handler.post(worker)
        }
    }

    fun removeItem(index: Int) {
        watchListService.removeAt(index)
        executar()
    }

    fun atualizaWatchList(acoes: MutableList<Ativo>) {
        this.watchListService = acoes
        executar()
    }

    fun parseDouble(valor: String?): Double {
        return if (valor != null) java.lang.Double.parseDouble(valor) else 0.00
    }

    fun parseInteger(valor: String?): Int {
        return if (valor != null) Integer.parseInt(valor) else 0
    }

    fun calculaVariacao(cotacaoAntiga: Double?, novaCotacao: Double?): Double {
        if (cotacaoAntiga!!.equals(0)) {
            return 0.00
        }
        return novaCotacao!! - cotacaoAntiga
    }

    inner class HeavyBinder : Binder() {
        val service: HeavyGearService
            get() = this@HeavyGearService
    }

    companion object {
        private val ACTION_HEAVYSERVICE = "ACTION_HEAVYSERVICE"
        private val LOG_TAG = "HeavyGearService"
        private val PREF_FREQUENCIA_ATUALIZACAO = "pref_frequencia_atualizacao"
    }
}
