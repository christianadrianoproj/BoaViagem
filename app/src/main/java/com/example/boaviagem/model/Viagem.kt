package com.example.boaviagem.model

import java.io.Serializable
import java.util.*

data class Viagem(
    val idViagem: Int?,
    var destino: String,
    var tipoViagem: Int,
    var dataChegada: Date?,
    var dataPartida: Date?,
    var orcamento: Double,
    var quantidadePessoas: Int,
    var usuario: Usuario?,
    var gastos: MutableList<Gasto>?
): Serializable {

    constructor(): this(null, "",0, null, null, 0.0, 0, null, mutableListOf())

    fun calculaGastos(): Double {
        var total: Double = 0.0
        if (this.gastos != null) {
            this.gastos!!.forEach { gasto ->
                total += gasto.valor
            }
        }
        return total
    }

    fun calculaPorcetagemGasto(): Double {
        var totalDespesa = calculaGastos()
        return (totalDespesa * 100) / orcamento
    }

    private fun getListaDiasGastos() : MutableList<Date> {
        val lista: MutableList<Date> = mutableListOf<Date>()
        if (this.gastos != null) {
            this.gastos!!.forEach {
                if (lista.indexOf(it.data) == -1) {
                    lista.add(it.data!!)
                }
            }
        }
        return lista
    }

    private fun getGastos(PData: Date) : MutableList<Gasto> {
        val lista: MutableList<Gasto> = mutableListOf<Gasto>()
        if (this.gastos != null) {
            this.gastos!!.forEach {
                if (it.data!! == PData) {
                    if (lista.indexOf(it) == -1) {
                        lista.add(it)
                    }
                }
            }
        }
        return lista
    }

    fun getDataGasto() : MutableList<DataGasto> {
        val lista: MutableList<DataGasto> = mutableListOf<DataGasto>()
        this.getListaDiasGastos().forEach { i ->
            val dataGasto = DataGasto(i, this.getGastos(i))
            lista.add(dataGasto)
        }
        return lista
    }

    override fun toString() : String {
        return this.destino + " - " + if (tipoViagem == 0) "Lazer" else "Negócios"
    }
}