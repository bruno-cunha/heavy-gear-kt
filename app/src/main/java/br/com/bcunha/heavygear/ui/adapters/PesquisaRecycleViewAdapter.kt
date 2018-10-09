package br.com.bcunha.heavygear.ui.adapters

import android.content.Context
import android.content.res.ColorStateList
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView

import br.com.bcunha.heavygear.R
import br.com.bcunha.heavygear.model.pojo.Ativo

/**
 * Created by bruno on 17/11/16.
 */

class PesquisaRecycleViewAdapter(var resultados: MutableList<Ativo>?, var watchList: MutableList<Ativo>) : RecyclerView.Adapter<PesquisaRecycleViewAdapter.PesquisaRecycleViewHolder>() {

    class PesquisaRecycleViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        internal val logo: ImageView
        internal val codigo: TextView
        internal val empresa: TextView
        internal val btnWatch: ImageButton

        init {
            logo = view.findViewById(R.id.imgAcao) as ImageView
            codigo = view.findViewById(R.id.codigo) as TextView
            empresa = view.findViewById(R.id.empresa) as TextView
            btnWatch = view.findViewById(R.id.btnWatch) as ImageButton
        }
    }

    init {
        comparaResultadosEWatch()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PesquisaRecycleViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.cardview_pesquisa, parent, false)
        return PesquisaRecycleViewHolder(v)
    }

    override fun onBindViewHolder(pesquisaRecycleViewHolder: PesquisaRecycleViewHolder, position: Int) {
        val context = pesquisaRecycleViewHolder.itemView.context
        var imgId = context.resources.getIdentifier(resultados!![position].codigo?.replace("\\d".toRegex(), "")?.toLowerCase(),
                "drawable",
                context.packageName)
        if (imgId == 0) {
            imgId = context.resources.getIdentifier("logo_indisponivel",
                    "drawable",
                    context.packageName)
        }
        pesquisaRecycleViewHolder.logo.setImageResource(imgId)
        pesquisaRecycleViewHolder.codigo.text = resultados!![position].codigo
        pesquisaRecycleViewHolder.empresa.text = resultados!![position].empresa
        if (resultados!![position].isInWatch) {
            pesquisaRecycleViewHolder.btnWatch.setBackgroundResource(R.drawable.ic_check_circle_black_36dp)
            pesquisaRecycleViewHolder.btnWatch.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.verdeClaro))
        } else {
            pesquisaRecycleViewHolder.btnWatch.setBackgroundResource(R.drawable.ic_add_circle_black_36dp)
            pesquisaRecycleViewHolder.btnWatch.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.cinza))
        }

        pesquisaRecycleViewHolder.btnWatch.setOnClickListener { view ->
            if (resultados!![position].isInWatch) {
                resultados!![position].isInWatch = false
                pesquisaRecycleViewHolder.btnWatch.setBackgroundResource(R.drawable.ic_add_circle_black_36dp)
                pesquisaRecycleViewHolder.btnWatch.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(view.context, R.color.cinza))
            } else {
                resultados!![position].isInWatch = true
                pesquisaRecycleViewHolder.btnWatch.setBackgroundResource(R.drawable.ic_check_circle_black_36dp)
                pesquisaRecycleViewHolder.btnWatch.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(view.context, R.color.verdeClaro))
            }
            if (resultados!![position].isInWatch && !watchList.contains(resultados!![position])) {
                watchList.add(resultados!![position])
            } else if (!resultados!![position].isInWatch && watchList.contains(resultados!![position])) {
                watchList.remove(resultados!![position])
            }
        }
    }

    override fun getItemCount(): Int {
        if (resultados == null) {
            return 0
        }
        return resultados!!.size
    }

    fun add(ativo: Ativo) {}

    fun remove(position: Int) {}

    fun update(novasAcoes: List<Ativo>) {
        resultados!!.clear()
        resultados!!.addAll(novasAcoes)
        comparaResultadosEWatch()
        notifyDataSetChanged()
    }

    fun comparaResultadosEWatch() {
        for (contador in resultados!!.indices) {
            if (watchList.contains(resultados!![contador])) {
                resultados!![contador].isInWatch = true
            }
        }
    }

    fun adicionaNoWatchLista(selecionados: List<Ativo>) {
        for (ativo in selecionados) {
            if (ativo.isInWatch && !watchList.contains(ativo)) {
                watchList.add(ativo)
            }
        }

    }
}
