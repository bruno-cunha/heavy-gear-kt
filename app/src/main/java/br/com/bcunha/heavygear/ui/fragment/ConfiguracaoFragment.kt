package br.com.bcunha.heavygear.ui.fragment

import android.os.Bundle
import android.preference.PreferenceFragment

import br.com.bcunha.heavygear.R

/**
 * Created by bruno on 15/05/17.
 */

class ConfiguracaoFragment : PreferenceFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        addPreferencesFromResource(R.xml.preferences)
    }
}
