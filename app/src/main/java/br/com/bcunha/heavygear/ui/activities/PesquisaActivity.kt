package br.com.bcunha.heavygear.ui.activities

import android.app.Activity
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v4.view.MenuItemCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem

import java.util.ArrayList

import br.com.bcunha.heavygear.R
import br.com.bcunha.heavygear.model.db.HeavyGearAssetsHelper
import br.com.bcunha.heavygear.model.pojo.Ativo
import br.com.bcunha.heavygear.ui.adapters.PesquisaRecycleViewAdapter

import android.support.v7.widget.LinearLayoutManager.VERTICAL

class PesquisaActivity : AppCompatActivity() {

    private var toolbar: Toolbar? = null
    private var recyclerView: RecyclerView? = null
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var pesquisaRecycleViewAdapter: PesquisaRecycleViewAdapter? = null
    private var pesqQuery = ""
    private var todasAcoesPesquisa: Boolean? = null
    private var heavyGearAssetsHelper: HeavyGearAssetsHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pesquisa)

        heavyGearAssetsHelper = HeavyGearAssetsHelper(this)
        heavyGearAssetsHelper!!.openDB()

        iniciaRecycleView()

        toolbar = findViewById(R.id.inc_toolbar) as Toolbar
        toolbar!!.title = pesqQuery
        setSupportActionBar(toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_pesquisa, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager

        val menuItem = menu.findItem(R.id.action_search)

        MenuItemCompat.setOnActionExpandListener(menuItem, object : MenuItemCompat.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem): Boolean {
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
                val intent = Intent()
                intent.putParcelableArrayListExtra("watchListService", pesquisaRecycleViewAdapter!!.watchList as ArrayList<Ativo>)
                setResult(Activity.RESULT_OK, intent)
                finish()
                return false
            }
        })

        val searchView = MenuItemCompat.getActionView(menuItem) as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.pesquisa_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                //                if (!query.equals(query)) {
                //                    pesqQuery = query;
                //                    toolbar.setTitle(pesqQuery);
                //                    pesquisaRecycleViewAdapter.update(heavyGearAssetsHelper.pesquisaAtivo(pesqQuery));
                //                }
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if (newText != pesqQuery) {
                    pesqQuery = newText
                    toolbar!!.title = pesqQuery
                    pesquisaRecycleViewAdapter!!.update(heavyGearAssetsHelper!!.pesquisaAtivo(pesqQuery))
                }
                return false
            }
        })
        MenuItemCompat.expandActionView(menuItem)
        if (todasAcoesPesquisa!!) {
            searchView.clearFocus()
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroy() {
        heavyGearAssetsHelper!!.closeDB()
        super.onDestroy()
    }

    private fun iniciaRecycleView() {
        todasAcoesPesquisa = true //PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext())
        //                 .getBoolean(PREF_TODOS_ATIVOS_PESQUISA, false);

        var resultados: MutableList<Ativo> = ArrayList()
        var watchList: MutableList<Ativo> = ArrayList()

        val intent = intent
        if (Intent.ACTION_SEARCH == intent.action) {
            pesqQuery = intent.getStringExtra(SearchManager.QUERY)
            resultados = heavyGearAssetsHelper!!.pesquisaAtivo(pesqQuery) as MutableList<Ativo>
            watchList = intent.getParcelableArrayListExtra<Ativo>("watchListService")
        } else {
            if (todasAcoesPesquisa!!) {
                resultados = heavyGearAssetsHelper!!.ativos as MutableList<Ativo>
            }
            if (intent.hasExtra("watchListService")) {
                watchList = intent.getParcelableArrayListExtra<Ativo>("watchListService")
            }
        }

        recyclerView = findViewById(R.id.recyclerView) as RecyclerView
        layoutManager = LinearLayoutManager(this)
        val dividerItemDecoration = DividerItemDecoration(recyclerView!!.context, VERTICAL)
        pesquisaRecycleViewAdapter = PesquisaRecycleViewAdapter(resultados, watchList)
        recyclerView!!.layoutManager = layoutManager
        recyclerView!!.addItemDecoration(dividerItemDecoration)
        recyclerView!!.adapter = pesquisaRecycleViewAdapter
    }

    companion object {

        val PREF_TODOS_ATIVOS_PESQUISA = "pref_todos_ativos_pesquisa"
    }
}
