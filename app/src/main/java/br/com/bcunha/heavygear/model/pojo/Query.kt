package br.com.bcunha.heavygear.model.pojo

import com.google.gson.annotations.SerializedName

import java.util.Date

/**
 * Created by bruno on 18/04/17.
 */

class Query(var count: Int, var results: Results) {

    @get:SerializedName("created")
    var created: Date = null!!

    @get:SerializedName("lang")
    var lang: String = ""
}
