package br.com.bcunha.heavygear.ui.fragment

import android.app.Dialog
import android.app.DialogFragment
import android.content.DialogInterface
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AlertDialog

import br.com.bcunha.heavygear.R
import br.com.bcunha.heavygear.ui.activities.ConfiguracaoActivity
import br.com.bcunha.heavygear.ui.activities.HeavyGearActivity

/**
 * Created by bruno on 05/06/17.
 */

class OrdemDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle): Dialog {
        val idOrdem = PreferenceManager.getDefaultSharedPreferences(activity.applicationContext)
                .getInt(ConfiguracaoActivity.PREF_ID_ORDEM, 2)
        val builder = AlertDialog.Builder(activity)
        builder.setTitle(R.string.ordem)
                .setSingleChoiceItems(R.array.ordem, idOrdem) { dialog, which ->
                    val heavyGearActivity = activity as HeavyGearActivity
                    heavyGearActivity.prefIdOrdem = which
                    heavyGearActivity.atualizaOrdemExibicao()
                    dismiss()
                }
        return builder.create()
    }
}
