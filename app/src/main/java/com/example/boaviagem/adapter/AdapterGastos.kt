package com.example.boaviagem.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.boaviagem.R
import com.example.boaviagem.model.Gasto
import com.example.boaviagem.util.Utils


class AdapterGastos(private val gastos: MutableList<Gasto>) :
    RecyclerView.Adapter<AdapterGastos.MyViewHolder>() {
    private val TYPE_HEADER = 0
    private val TYPE_ITEM = 1

    var onItemClick: ((Gasto) -> Unit)? = null

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textViewDestino: TextView = itemView.findViewById(R.id.textDescricaoGasto)
        var textViewDataChegada: TextView = itemView.findViewById(R.id.textValorGasto)


        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(gastos[adapterPosition])
            }
        }

        fun bind(gasto: Gasto) {
            textViewDestino.text = gasto.descricao
            val gastoConvetido = Utils.formatarValor(gasto.valor)
            textViewDataChegada.text = "R$: $gastoConvetido"
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_layout_gastos_recycler_view, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return this.gastos.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(gastos[position])
    }

}