package com.example.boaviagem.model

import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*


data class DataGasto(
    var data: Date?,
    var gastos: MutableList<Gasto>?
): Serializable {

    constructor() : this(null, mutableListOf())

    override fun toString() : String {
        val format = SimpleDateFormat("dd/MM/yyyy")
        return format.format(this.data)
    }
}
