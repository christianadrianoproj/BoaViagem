package com.example.boaviagem.model

import java.io.Serializable
import java.util.*

data class Gasto(
    val idGasto: Int?,
    val viagem: Viagem?,
    val tipo: Any,
    val valor: Double,
    val data: Date?,
    val descricao: String,
    val local: String
) : Serializable {

    constructor(): this(null, null, "", 0.0, null, "","")

    public override fun toString() : String {
        return this.descricao + " - " + this.local
    }
}