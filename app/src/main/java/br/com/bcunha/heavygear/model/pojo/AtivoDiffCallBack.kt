package br.com.bcunha.heavygear.model.pojo

import android.support.v7.util.DiffUtil

/**
 * Created by bruno on 24/07/17.
 */

class AtivoDiffCallBack(val newAcoes: List<Ativo>, val oldAcoes: List<Ativo>) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldAcoes.size
    }

    override fun getNewListSize(): Int {
        return newAcoes.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldAcoes[oldItemPosition].codigo == newAcoes[newItemPosition].codigo
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldAtivo = oldAcoes[oldItemPosition]
        val newAtivo = newAcoes[newItemPosition]

        if (oldAtivo.isViewExpanded) {
            newAcoes[newItemPosition].isViewExpanded = true
        }

        return oldAtivo.getCotacao() == newAtivo.getCotacao()
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        return super.getChangePayload(oldItemPosition, newItemPosition)
    }

}
