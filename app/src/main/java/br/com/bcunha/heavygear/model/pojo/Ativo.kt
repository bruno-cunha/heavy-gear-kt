package br.com.bcunha.heavygear.model.pojo

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Parcel
import android.os.Parcelable
import android.support.v4.content.ContextCompat

import br.com.bcunha.heavygear.R

/**
 * Created by BRUNO on 18/09/2016.
 */
class Ativo : Parcelable {
    var codigo: String? = null
    var empresa: String? = null
    private var tipo: String? = null
    private var cotacao: Double = 0.toDouble()
    var variacao: Double = 0.toDouble()
    var abertura: Double = 0.toDouble()
    var maximaDia: Double = 0.toDouble()
    var minimaDia: Double = 0.toDouble()
    var maximaAno: Double = 0.toDouble()
    var minimaAno: Double = 0.toDouble()
    var cotaocaoDolar: Double = 0.toDouble()
    var volumeNegociacao: Int = 0
    var isInWatch: Boolean = false
    var isViewExpanded = false
    var originalHeight: Int = 0
    var index: Int = 0
    var isRefresh: Boolean = false

    constructor(codigo: String) {
        this.codigo = codigo
    }

    constructor(codigo: String, empresa: String, tipo: String, cotacao: Double) {
        this.codigo = codigo
        this.empresa = empresa
        this.tipo = tipo
        this.cotacao = cotacao
    }

    constructor(codigo: String, empresa: String, tipo: String, cotacao: Double, inWatch: Boolean) {
        this.codigo = codigo
        this.empresa = empresa
        this.tipo = tipo
        this.cotacao = cotacao
        this.isInWatch = inWatch
    }

    constructor(codigo: String, empresa: String, tipo: String, cotacao: Double, variacao: Double, inWatch: Boolean) {
        this.codigo = codigo
        this.empresa = empresa
        this.tipo = tipo
        this.cotacao = cotacao
        this.variacao = variacao
        this.isInWatch = inWatch
    }

    constructor(codigo: String, empresa: String, tipo: String, cotacao: Double, variacao: Double, maximaDia: Double, minimaDia: Double, maximaAno: Double, minimaAno: Double, volumeNegociacao: Int, inWatch: Boolean) {
        this.codigo = codigo
        this.empresa = empresa
        this.tipo = tipo
        this.cotacao = cotacao
        this.variacao = variacao
        this.maximaDia = maximaDia
        this.minimaDia = minimaDia
        this.maximaAno = maximaAno
        this.minimaAno = minimaAno
        this.volumeNegociacao = volumeNegociacao
        this.isInWatch = inWatch
    }

    fun getTipo(): String {
        if (tipo == null) {
            return ""
        } else {
            return tipo.toString()
        }
    }

    fun setTipo(tipo: String) {
        this.tipo = tipo
    }

    fun getCotacao(): Double {
        return cotacao
    }

    fun setCotacao(cotacao: Double) {
        isRefresh = if (this.cotacao != cotacao) true else false
        this.cotacao = cotacao
    }

    val variacaoFormat: String
        get() {
            if (variacao > 0) {
                return "(+" + String.format("%.2f", variacao) + ")"
            } else if (variacao > 0) {
                return "(-" + String.format("%.2f", variacao * 1) + ")"
            } else {
                return "(" + String.format("%.2f", variacao) + ")"
            }
        }

    fun getImgId(context: Context): Int {
        var imgId = context.resources.getIdentifier(codigo?.replace("\\d".toRegex(), "")?.toLowerCase(),
                "drawable",
                context.packageName)
        if (imgId == 0) {
            imgId = context.resources.getIdentifier("logo_indisponivel",
                    "drawable",
                    context.packageName)
        }
        return imgId
    }

    fun getCor(context: Context): ColorStateList {
        if (getTipo() != "MOEDA") {
            if (variacao > 0) {
                return ColorStateList.valueOf(ContextCompat.getColor(context, R.color.verde))
            } else if (variacao < 0) {
                return ColorStateList.valueOf(ContextCompat.getColor(context, R.color.vermelho))
            } else {
                return ColorStateList.valueOf(ContextCompat.getColor(context, R.color.textoSecundario))
            }
        } else {
            return ColorStateList.valueOf(ContextCompat.getColor(context, R.color.preto))
        }
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false

        val ativo = o as Ativo?

        return codigo == ativo!!.codigo

    }

    override fun hashCode(): Int {
        return codigo!!.hashCode()
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, i: Int) {
        parcel.writeString(codigo)
        parcel.writeString(empresa)
        parcel.writeString(tipo)
        parcel.writeDouble(cotacao)
        parcel.writeDouble(cotaocaoDolar)
        parcel.writeDouble(variacao)
        parcel.writeDouble(maximaDia)
        parcel.writeDouble(minimaDia)
        parcel.writeDouble(maximaAno)
        parcel.writeDouble(minimaAno)
        parcel.writeInt(volumeNegociacao)
        parcel.writeInt(if (isInWatch) 1 else 0)
        parcel.writeInt(if (isViewExpanded) 1 else 0)
        parcel.writeInt(originalHeight)
        parcel.writeInt(index)
        parcel.writeInt(if (isRefresh) 1 else 0)
    }

    // Objeto
    constructor(`in`: Parcel) {
        this.codigo = `in`.readString()
        this.empresa = `in`.readString()
        this.tipo = `in`.readString()
        this.cotacao = `in`.readDouble()
        this.cotaocaoDolar = `in`.readDouble()
        this.variacao = `in`.readDouble()
        this.maximaDia = `in`.readDouble()
        this.minimaDia = `in`.readDouble()
        this.maximaAno = `in`.readDouble()
        this.minimaAno = `in`.readDouble()
        this.volumeNegociacao = `in`.readInt()
        this.isInWatch = `in`.readInt() != 0
        this.isViewExpanded = `in`.readInt() != 0
        this.originalHeight = `in`.readInt()
        this.index = `in`.readInt()
        this.isRefresh = `in`.readInt() != 0
    }

    companion object CREATOR : Parcelable.Creator<Ativo> {
        override fun createFromParcel(parcel: Parcel): Ativo {
            return Ativo(parcel)
        }

        override fun newArray(size: Int): Array<Ativo?> {
            return arrayOfNulls(size)
        }
    }
}