package com.example.boaviagem.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.boaviagem.R
import com.example.boaviagem.model.DataGasto

class AdapterGastosPorData(private val datas: MutableList<DataGasto>) :
RecyclerView.Adapter<AdapterGastosPorData.MyViewHolder>() {

    private val TYPE_HEADER = 0
    private val TYPE_ITEM = 1

    var onItemClick: ((String) -> Unit)? = null

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textViewGroupData: TextView = itemView.findViewById(R.id.textGroupData)
        var childRecyclerView: RecyclerView = itemView.findViewById(R.id.recyclerViewGastosPorData)

        init {
        }

        fun bind(data: DataGasto) {
            textViewGroupData.text = data.toString()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_layout_datas_gastos_recycler_view, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return this.datas.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(this.datas[position])

        val childRecyclerAdapter = AdapterGastos(this.datas[position].gastos!!)
        holder.childRecyclerView!!.adapter = childRecyclerAdapter
    }
}