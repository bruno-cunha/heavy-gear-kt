package br.com.bcunha.heavygear.ui.activities

import android.app.Activity
import android.app.DialogFragment
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.ServiceConnection
import android.content.SharedPreferences
import android.content.res.AssetManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Typeface
import android.os.Bundle
import android.os.IBinder
import android.os.Parcelable
import android.preference.PreferenceManager
import android.support.design.widget.NavigationView
import android.support.v4.content.LocalBroadcastManager
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView

import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

import java.io.IOException
import java.io.InputStream
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Calendar
import java.util.Collections

import br.com.bcunha.heavygear.R
import br.com.bcunha.heavygear.model.api.alphavantage.ApiAlphaVantageKey
import br.com.bcunha.heavygear.model.db.HeavyGearAssetsHelper
import br.com.bcunha.heavygear.model.pojo.Ativo
import br.com.bcunha.heavygear.model.pojo.ordem.OrdemAlfabetica
import br.com.bcunha.heavygear.model.pojo.ordem.OrdemAlta
import br.com.bcunha.heavygear.model.pojo.ordem.OrdemBaixa
import br.com.bcunha.heavygear.model.service.HeavyGearService
import br.com.bcunha.heavygear.model.service.HeavyGearService.HeavyBinder
import br.com.bcunha.heavygear.ui.adapters.HeavyGearRecycleViewAdapter
import br.com.bcunha.heavygear.ui.fragment.OrdemDialogFragment

import br.com.bcunha.heavygear.R.menu.menu_heavy_gear

class HeavyGearActivity : AppCompatActivity() {

    var prefTodasAcoesInicio: Boolean = false
    var prefExibeVaricao: Boolean = false
    var prefIdOrdem: Int = 0

    private val formatDate = SimpleDateFormat("dd/mm/yyyy HH:mm:ss")
    private var heavyGearAssetsHelper: HeavyGearAssetsHelper? = null
    private var typeFace: Typeface? = null
    private var toolbar: Toolbar? = null
    private var appNome: TextView? = null
    private var toolbarTitle: TextView? = null
    private var drawerLayout: DrawerLayout? = null
    private var ultimaSincronizacao: TextView? = null
    private var recyclerView: RecyclerView? = null
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var heavyGearRecycleViewAdapter: HeavyGearRecycleViewAdapter? = null
    private var sharedPreferences: SharedPreferences? = null
    private var container: LinearLayout? = null
    private var adView: AdView? = null
    private var heavyGearServiceBound: HeavyGearService? = null
    private var isBound: Boolean? = false

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as HeavyBinder
            heavyGearServiceBound = binder.service
            isBound = true
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            heavyGearServiceBound = null
            isBound = false
        }
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (ACTION_HEAVYSERVICE == intent.action) {

                heavyGearRecycleViewAdapter!!.updateItem(intent.getParcelableExtra<Parcelable>("ativo") as Ativo,
                        intent.extras!!.getInt("index"))
                atualizaUltimaSincronizacao()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_heavy_gear)

        // SQLite
        heavyGearAssetsHelper = HeavyGearAssetsHelper(this)
        heavyGearAssetsHelper!!.openDB()

        // ToolBar
        toolbar = findViewById(R.id.inc_toolbar) as Toolbar
        toolbar!!.setNavigationIcon(R.drawable.ic_menu_black_36dp)

        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        toolbar!!.setNavigationOnClickListener { drawerLayout!!.openDrawer(1) }
        typeFace = Typeface.createFromAsset(assets, "fonts/Arizonia-Regular.ttf")
        toolbarTitle = toolbar!!.findViewById(R.id.toolbarTitle) as TextView
        toolbarTitle!!.typeface = typeFace

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false)

        //Ads
        MobileAds.initialize(this, ApiAlphaVantageKey.AppId)
        container = findViewById(R.id.container) as LinearLayout
        adView = AdView(this)
        adView!!.adSize = AdSize.SMART_BANNER
        //adView!!.layoutParams = AdView.LayoutParams(AdView.LayoutParams.MATCH_PARENT, AdView.LayoutParams.WRAP_CONTENT)
        adView!!.adUnitId = ApiAlphaVantageKey.AdUnitId
        container!!.addView(adView)

        val adRequest = AdRequest.Builder().build()
        adView!!.loadAd(adRequest)

        iniciaRecycleView()
        iniciatNavigationDrawer()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(menu_heavy_gear, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == R.id.search) {
            val intent = Intent(this, PesquisaActivity::class.java)
            intent.putParcelableArrayListExtra("watchListService", heavyGearRecycleViewAdapter!!.watchList as ArrayList<Ativo>)
            startActivityForResult(intent, REQUEST_PESQUISA)
        } else if (id == R.id.ordem) {
            val dialogFragment = OrdemDialogFragment()
            dialogFragment.show(fragmentManager, "ordem_exibicao")
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onStart() {
        super.onStart()

        // Bind Serviço
        if (!isBound!!) {
            val intent = Intent(this, HeavyGearService::class.java)
            intent.putParcelableArrayListExtra("watchListService", heavyGearRecycleViewAdapter!!.watchList as ArrayList<Ativo>)
            bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
        }
    }

    override fun onResume() {
        super.onResume()

        // Registra o recevier do serviço
        LocalBroadcastManager.getInstance(this).registerReceiver(this.receiver, IntentFilter(ACTION_HEAVYSERVICE))
    }

    override fun onPause() {
        super.onPause()

        // Desregistra o receier do serviço
        LocalBroadcastManager.getInstance(this).unregisterReceiver(this.receiver)
    }

    override fun onStop() {
        super.onStop()
        // Salva watchListService
        salvaWatchList()

        // UnBind Serviço
        if (isBound!!) {
            unbindService(serviceConnection)
            isBound = false
        }
    }

    override fun onDestroy() {
        heavyGearAssetsHelper!!.closeDB()
        super.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (requestCode == REQUEST_PESQUISA && resultCode == Activity.RESULT_OK) {
            heavyGearRecycleViewAdapter!!.updateAll(data.getParcelableArrayListExtra<Parcelable>("watchListService") as ArrayList<Ativo>)
            heavyGearServiceBound!!.atualizaWatchList(heavyGearRecycleViewAdapter!!.watchList as MutableList<Ativo>)
            atualizaOrdemExibicao()
        } else if (requestCode == REQUEST_CONFIGURACAO && resultCode == Activity.RESULT_OK) {
            atualizaConfiguracoes()
        }
    }

    private fun iniciaRecycleView() {
        // Carrega watchListService
        var watchList: MutableList<Ativo> = ArrayList()
        initPrefs()

        val json = sharedPreferences!!.getString("watchListService", "")
        if (!json!!.isEmpty()) {
            val type = object : TypeToken<List<Ativo>>() {

            }.type
            watchList = Gson().fromJson<MutableList<Ativo>>(json, type)
        } else if (watchList.size == 0) {
            watchList.add(Ativo("PETR3", "Petrobras", "", 00.00, true))
        }

        // RecyclerView
        layoutManager = LinearLayoutManager(this)
        heavyGearRecycleViewAdapter = HeavyGearRecycleViewAdapter(this, watchList)
        recyclerView = findViewById(R.id.recyclerView) as RecyclerView
        recyclerView!!.layoutManager = layoutManager
        recyclerView!!.adapter = heavyGearRecycleViewAdapter

        val simpleItemTouchCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
                val index = viewHolder.adapterPosition
                heavyGearRecycleViewAdapter!!.remove(index)
                heavyGearServiceBound!!.removeItem(index)
            }

            override fun onMoved(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, fromPos: Int, target: RecyclerView.ViewHolder, toPos: Int, x: Int, y: Int) {
                super.onMoved(recyclerView, viewHolder, fromPos, target, toPos, x, y)
            }
        }

        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun iniciatNavigationDrawer() {
        val navigationView = findViewById(R.id.navigation_view) as NavigationView
        navigationView.setNavigationItemSelectedListener { menuItem ->
            val id = menuItem.itemId

            when (id) {
                R.id.configuracoes -> {
                    startActivityForResult(Intent(this@HeavyGearActivity, ConfiguracaoActivity::class.java), REQUEST_CONFIGURACAO)
                    drawerLayout!!.closeDrawers()
                }
                R.id.sobre -> {
                    startActivity(Intent(this@HeavyGearActivity, SobreActivity::class.java))
                    drawerLayout!!.closeDrawers()
                }
            }
            true
        }
        val header = navigationView.getHeaderView(0)
        appNome = header.findViewById(R.id.app_nome) as TextView
        appNome!!.typeface = typeFace
        ultimaSincronizacao = header.findViewById(R.id.ultima_sincronizacao_datahora) as TextView
        drawerLayout = findViewById(R.id.drawer) as DrawerLayout

        val actionBarDrawerToggle = object : ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {

            override fun onDrawerClosed(v: View?) {
                super.onDrawerClosed(v)
            }

            override fun onDrawerOpened(v: View?) {
                super.onDrawerOpened(v)
            }
        }
        drawerLayout!!.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
    }

    fun initPrefs() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.applicationContext)
        prefTodasAcoesInicio = false//sharedPreferences.getBoolean(ConfiguracaoActivity.PREF_TODAS_ACOES_INICIO, false);
        prefExibeVaricao = sharedPreferences!!.getBoolean(ConfiguracaoActivity.PREF_EXIBE_VARIACAO, false)
        prefIdOrdem = sharedPreferences!!.getInt(ConfiguracaoActivity.PREF_ID_ORDEM, 2)
    }

    private fun atualizaConfiguracoes() {
        val oldTodasAcoesInicio = prefTodasAcoesInicio
        initPrefs()
        if (prefTodasAcoesInicio) {
            heavyGearRecycleViewAdapter!!.updateAll(heavyGearAssetsHelper!!.ativos)
        } else if (prefTodasAcoesInicio != oldTodasAcoesInicio) {
            heavyGearRecycleViewAdapter!!.updateAll(heavyGearAssetsHelper!!.pesquisaAtivo("PETR3"))
        }
        heavyGearRecycleViewAdapter!!.updateExibeVariacao(sharedPreferences!!.getBoolean(ConfiguracaoActivity.PREF_EXIBE_VARIACAO, false))
        if (isBound!!) {
            heavyGearServiceBound!!.atualizaTimer()
        }
        salvaWatchList()
    }

    private fun atualizaUltimaSincronizacao() {
        val ultimaSincronizacao = formatDate.format(Calendar.getInstance().time)
        this.ultimaSincronizacao!!.text = ultimaSincronizacao
        val preferencesEditor = sharedPreferences!!.edit()
        preferencesEditor.putString("ultimaSincronizacao", ultimaSincronizacao)
        preferencesEditor.commit()
    }

    private fun salvaWatchList() {
        val preferencesEditor = sharedPreferences!!.edit()
        if (heavyGearRecycleViewAdapter!!.watchList!!.size == 0) {
            preferencesEditor.putString("watchListService", "")
        } else {
            val json = Gson().toJson(heavyGearRecycleViewAdapter!!.watchList)
            preferencesEditor.putString("watchListService", json)
        }
        preferencesEditor.commit()
    }

    private fun salvaOrdem() {
        val preferencesEditor = sharedPreferences!!.edit()
        preferencesEditor.putInt("pref_id_ordem", prefIdOrdem)
        preferencesEditor.commit()
    }

    private fun carregaBitmapAsset(strName: String): Bitmap {
        val assetManager = assets
        var istr: InputStream? = null
        try {
            istr = assetManager.open(strName)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        val bitmap = BitmapFactory.decodeStream(istr)
        return bitmap
    }

    fun atualizaOrdemExibicao() {
        if (prefIdOrdem == 0) {
            Collections.sort(heavyGearServiceBound!!.watchListService, OrdemAlta())
        } else if (prefIdOrdem == 1) {
            Collections.sort(heavyGearServiceBound!!.watchListService, OrdemBaixa())
        } else if (prefIdOrdem == 2) {
            Collections.sort(heavyGearServiceBound!!.watchListService, OrdemAlfabetica())
        }
        heavyGearServiceBound!!.executar()
        salvaOrdem()
    }

    companion object {

        val REQUEST_PESQUISA = 1
        val REQUEST_CONFIGURACAO = 2

        private val ACTION_HEAVYSERVICE = "ACTION_HEAVYSERVICE"
    }
}
