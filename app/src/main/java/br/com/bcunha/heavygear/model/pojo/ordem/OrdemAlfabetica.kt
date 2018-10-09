package br.com.bcunha.heavygear.model.pojo.ordem

import java.util.Comparator

import br.com.bcunha.heavygear.model.pojo.Ativo

/**
 * Created by bruno on 06/06/17.
 */

class OrdemAlfabetica : Comparator<Ativo> {

    override fun compare(o1: Ativo, o2: Ativo): Int {
        //if(o2.getTipo().equals("MOEDA")) {
        //    return 0;
        //} else {
        return o1.codigo?.compareTo(o2.codigo.toString(), ignoreCase = true)!!
        //}
    }
}
