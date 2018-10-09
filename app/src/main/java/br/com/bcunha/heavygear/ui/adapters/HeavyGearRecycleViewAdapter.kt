package br.com.bcunha.heavygear.ui.adapters

import android.animation.ValueAnimator
import android.content.Context
import android.preference.PreferenceManager
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView

import java.util.ArrayList

import br.com.bcunha.heavygear.R
import br.com.bcunha.heavygear.model.pojo.Ativo
import br.com.bcunha.heavygear.model.pojo.Quote
import br.com.bcunha.heavygear.ui.activities.ConfiguracaoActivity

/**
 * Created by BRUNO on 18/10/2016.
 */

class HeavyGearRecycleViewAdapter(private val context: Context, var watchList: MutableList<Ativo>?) : RecyclerView.Adapter<HeavyGearRecycleViewAdapter.HeavyGearRecycleViewHolder>() {

    class HeavyGearRecycleViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        internal val cardView: CardView
        internal val logo: ImageView
        internal val codigo: TextView
        internal val empresa: TextView
        internal val moeda: TextView
        internal val cotacao: TextView
        internal val variacao: TextView
        internal val minimaDia: TextView
        internal val maximaDia: TextView
        internal val abertura: TextView
        internal val volume: TextView
        internal var ativo: Ativo? = null
        private val relativeSecundario: RelativeLayout

        init {
            cardView = view.findViewById(R.id.cardView_heavy_gear) as CardView
            logo = view.findViewById(R.id.imgAcao) as ImageView
            codigo = view.findViewById(R.id.codigo) as TextView
            empresa = view.findViewById(R.id.empresa) as TextView
            moeda = view.findViewById(R.id.moeda) as TextView
            cotacao = view.findViewById(R.id.cotacao) as TextView
            variacao = view.findViewById(R.id.variacao) as TextView
            relativeSecundario = view.findViewById(R.id.relativeSecundario) as RelativeLayout
            minimaDia = view.findViewById(R.id.minimaDia) as TextView
            maximaDia = view.findViewById(R.id.maximaDia) as TextView
            abertura = view.findViewById(R.id.abertura) as TextView
            volume = view.findViewById(R.id.volume) as TextView

            /*cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    if (ativo.getOriginalHeight() == 0) {
                        if (ativo.isViewExpanded()) {
                            ativo.setOriginalHeight((int) (view.getHeight() / 2.5));
                        } else {
                            ativo.setOriginalHeight(view.getHeight());
                        }
                    }

                    ValueAnimator valueAnimator;
                    if (ativo.isViewExpanded()) {
                        relativeSecundario.setVisibility(View.GONE);
                        relativeSecundario.setEnabled(false);
                        ativo.setViewExpanded(false);
                        valueAnimator = ValueAnimator.ofInt(ativo.getOriginalHeight() + (int) (ativo.getOriginalHeight() * 1.5), ativo.getOriginalHeight());
                    } else {
                        relativeSecundario.setVisibility(View.VISIBLE);
                        relativeSecundario.setEnabled(true);
                        ativo.setViewExpanded(true);
                        valueAnimator = ValueAnimator.ofInt(ativo.getOriginalHeight(), ativo.getOriginalHeight() + (int) (ativo.getOriginalHeight() * 1.5));
                    }
                    valueAnimator.setDuration(200);
                    valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
                    valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        public void onAnimationUpdate(ValueAnimator animation) {
                            Integer value = (Integer) animation.getAnimatedValue();
                            view.getLayoutParams().height = value.intValue();
                            view.requestLayout();
                        }
                    });
                    valueAnimator.start();
                }
            });*/
        }
    }

    var prefExibeVaricao: Boolean = false
    private val animation: Animation

    init {
        this.prefExibeVaricao = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(ConfiguracaoActivity.PREF_EXIBE_VARIACAO, false)

        animation = AlphaAnimation(0.0f, 1.0f)
        animation.duration = 100
        animation.startOffset = 20
        animation.repeatMode = Animation.REVERSE
        animation.repeatCount = 5
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeavyGearRecycleViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.cardview_heavy_gear, parent, false)
        return HeavyGearRecycleViewHolder(v)
    }

    override fun onBindViewHolder(heavyGearRecycleViewHolder: HeavyGearRecycleViewHolder, position: Int) {
        heavyGearRecycleViewHolder.ativo = watchList!![position]
        heavyGearRecycleViewHolder.logo.setImageResource(heavyGearRecycleViewHolder.ativo!!.getImgId(context))
        heavyGearRecycleViewHolder.codigo.text = heavyGearRecycleViewHolder.ativo!!.codigo
        heavyGearRecycleViewHolder.empresa.text = heavyGearRecycleViewHolder.ativo!!.empresa
        heavyGearRecycleViewHolder.moeda.setTextColor(heavyGearRecycleViewHolder.ativo!!.getCor(context))
        heavyGearRecycleViewHolder.cotacao.setText(String.format("%.2f", heavyGearRecycleViewHolder.ativo!!.getCotacao()))
        heavyGearRecycleViewHolder.cotacao.setTextColor(heavyGearRecycleViewHolder.ativo!!.getCor(context))
        if (heavyGearRecycleViewHolder.ativo!!.getTipo() != "MOEDA") {
            heavyGearRecycleViewHolder.variacao.text = heavyGearRecycleViewHolder.ativo!!.variacaoFormat
        } else {
            heavyGearRecycleViewHolder.variacao.text = "$ " + String.format("%.2f", heavyGearRecycleViewHolder.ativo!!.cotaocaoDolar)
        }
        heavyGearRecycleViewHolder.variacao.setTextColor(heavyGearRecycleViewHolder.ativo!!.getCor(context))
        heavyGearRecycleViewHolder.minimaDia.setText(String.format("%.2f", heavyGearRecycleViewHolder.ativo!!.minimaDia))
        heavyGearRecycleViewHolder.maximaDia.setText(String.format("%.2f", heavyGearRecycleViewHolder.ativo!!.maximaDia))
        heavyGearRecycleViewHolder.abertura.setText(String.format("%.2f", heavyGearRecycleViewHolder.ativo!!.abertura))
        heavyGearRecycleViewHolder.volume.setText(String.format("%d", heavyGearRecycleViewHolder.ativo!!.volumeNegociacao))

        if (prefExibeVaricao) {
            heavyGearRecycleViewHolder.variacao.visibility = View.VISIBLE
        } else {
            heavyGearRecycleViewHolder.variacao.visibility = View.GONE
        }

        /*if (heavyGearRecycleViewHolder.ativo.isViewExpanded()) {
            heavyGearRecycleViewHolder.relativeSecundario.setVisibility(View.VISIBLE);
            heavyGearRecycleViewHolder.relativeSecundario.setEnabled(true);
        } else {
            heavyGearRecycleViewHolder.relativeSecundario.setVisibility(View.GONE);
            heavyGearRecycleViewHolder.relativeSecundario.setEnabled(false);
            if (heavyGearRecycleViewHolder.ativo.getOriginalHeight() > 0) {
                heavyGearRecycleViewHolder.cardView.getLayoutParams().height = heavyGearRecycleViewHolder.ativo.getOriginalHeight();
            }
        }*/
        if (heavyGearRecycleViewHolder.ativo!!.isRefresh) {
            heavyGearRecycleViewHolder.moeda.startAnimation(animation)
            heavyGearRecycleViewHolder.cotacao.startAnimation(animation)
            heavyGearRecycleViewHolder.variacao.startAnimation(animation)
        }
        heavyGearRecycleViewHolder.cardView.requestLayout()
    }

    override fun getItemCount(): Int {
        if (watchList == null) {
            return 0
        }
        return watchList!!.size
    }

    fun add(ativo: Ativo) {
        watchList!!.add(ativo)
        notifyDataSetChanged()
    }

    fun remove(position: Int) {
        watchList!!.removeAt(position)

        notifyItemRemoved(position)
    }

    fun update(novasAcoes: List<Ativo>) {
        if (novasAcoes.size != watchList!!.size) {
            return
        }

        val oldAcoes = ArrayList<Ativo>()
        oldAcoes.addAll(watchList!!)
        watchList!!.clear()
        watchList!!.addAll(novasAcoes)
        for (contador in novasAcoes.indices) {
            if (novasAcoes[contador].getCotacao() != oldAcoes[contador].getCotacao()) {
                notifyItemChanged(contador)
            }
        }
    }

    fun updateAll(novasAcoes: List<Ativo>) {
        watchList!!.clear()
        watchList!!.addAll(novasAcoes)
        notifyDataSetChanged()
    }

    fun updateItem(ativo: Ativo?, index: Int?) {
        if (ativo != null && index != null && index <= watchList!!.size - 1) {
            watchList!![index!!] = ativo
            notifyItemChanged(index)
        }
    }

    fun updateExibeVariacao(prefExibeVaricao: Boolean?) {
        this.prefExibeVaricao = prefExibeVaricao!!
        notifyDataSetChanged()
    }

    companion object {

        fun createFromQuote(context: Context, quoteAcoes: List<Quote>): HeavyGearRecycleViewAdapter {
            val acoes = ArrayList<Ativo>()

            for (quote in quoteAcoes) {
                acoes.add(Ativo(quote.getsymbol(),
                        quote.name,
                        "",
                        java.lang.Double.parseDouble(quote.lastTradePriceOnly)))
            }

            return HeavyGearRecycleViewAdapter(context, acoes)
        }
    }
}
