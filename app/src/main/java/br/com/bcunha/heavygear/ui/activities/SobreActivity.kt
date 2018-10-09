package br.com.bcunha.heavygear.ui.activities

import android.graphics.Typeface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.widget.TextView

import br.com.bcunha.heavygear.R

class SobreActivity : AppCompatActivity() {

    private var toolbar: Toolbar? = null
    private val typeFace: Typeface? = null
    private val toolbarTitle: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sobre)

        toolbar = findViewById(R.id.inc_toolbar) as Toolbar

        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
    }
}
