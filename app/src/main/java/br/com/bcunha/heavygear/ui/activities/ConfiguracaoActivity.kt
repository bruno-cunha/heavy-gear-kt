package br.com.bcunha.heavygear.ui.activities

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Typeface
import android.preference.Preference
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.widget.TextView

import br.com.bcunha.heavygear.R
import br.com.bcunha.heavygear.ui.fragment.ConfiguracaoFragment

class ConfiguracaoActivity : AppCompatActivity(), SharedPreferences.OnSharedPreferenceChangeListener {

    private var toolbar: Toolbar? = null
    private var typeFace: Typeface? = null
    private var toolbarTitle: TextView? = null
    private var sharedPreferences: SharedPreferences? = null
    private var prefAtualizar = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_configuracao)

        toolbar = findViewById(R.id.inc_toolbar) as Toolbar

        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        typeFace = Typeface.createFromAsset(assets, "fonts/Arizonia-Regular.ttf")
        toolbarTitle = toolbar!!.findViewById(R.id.toolbarTitle) as TextView
        toolbarTitle!!.typeface = typeFace

        fragmentManager.beginTransaction().replace(R.id.fragment_container, ConfiguracaoFragment()).commit()

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.applicationContext)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == android.R.id.home) {
            val intent = Intent()
            intent.putExtra("pref_atualizar", prefAtualizar)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        sharedPreferences!!.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        sharedPreferences!!.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        if (!prefAtualizar) {
            prefAtualizar = true
        }
    }

    companion object {

        //public static final String PREF_TODAS_ACOES_INICIO = "pref_todas_acoes_inicio";
        val PREF_TODAS_ACOES_PESQUISA = "pref_todas_acoes_pesquisa"
        val PREF_EXIBE_VARIACAO = "pref_exibe_variacao"
        val PREF_FREQUENCIA_ATUALIZACAO = "pref_frequencia_atualizacao"
        val PREF_ID_ORDEM = "pref_id_ordem"
    }
}
