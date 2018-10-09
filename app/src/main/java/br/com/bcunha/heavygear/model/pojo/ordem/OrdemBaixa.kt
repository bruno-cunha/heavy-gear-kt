package br.com.bcunha.heavygear.model.pojo.ordem

import java.util.Comparator

import br.com.bcunha.heavygear.model.pojo.Ativo

/**
 * Created by bruno on 06/06/17.
 */

class OrdemBaixa : Comparator<Ativo> {

    override fun compare(o1: Ativo, o2: Ativo): Int {
        //if (o2.getTipo().equals("MOEDA")) {
        //    return 0;
        //} else
        if (o1.variacao == o2.variacao) {
            return 0
        } else if (o1.variacao < o2.variacao) {
            return -1
        }
        return 1
    }
}
