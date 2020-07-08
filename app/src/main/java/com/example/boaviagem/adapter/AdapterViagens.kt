package com.example.boaviagem.adapter

import android.graphics.Color
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.boaviagem.R
import com.example.boaviagem.model.Viagem
import com.example.boaviagem.util.Utils


class AdapterViagens(private val viagens: MutableList<Viagem>) :
    RecyclerView.Adapter<AdapterViagens.MyViewHolder>() {
    private val TYPE_HEADER = 0
    private val TYPE_ITEM = 1

    var onItemClick: ((Viagem) -> Unit)? = null
    var onEditar: ((Viagem) -> Boolean)? = null
    var onNovoGasto: ((Viagem) -> Boolean)? = null
    var onGastosRealizados: ((Viagem) -> Boolean)? = null
    var onExcluir: ((Viagem) -> Boolean)? = null

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnCreateContextMenuListener {
        var textViewDestino : TextView = itemView.findViewById(R.id.textDestinoViagem)
        var textViewDatas : TextView = itemView.findViewById(R.id.textDatas)
        var textViewGastos : TextView = itemView.findViewById(R.id.textGastos)
        var progressBarOrcamento : ProgressBar = itemView.findViewById(R.id.progressBarOrcamento)
        var textPorcetagem : TextView = itemView.findViewById(R.id.textPorcetagem)
        var imageViewTipo : ImageView = itemView.findViewById(R.id.imageViewTipoViagem)

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(viagens[adapterPosition])
            }
            itemView.setOnCreateContextMenuListener(this)
        }

        fun bind(viagem : Viagem) {
            textViewDestino.text = viagem.destino
            textViewDatas.text = Utils.formatarData(viagem.dataChegada!!) + " a "
            if (viagem.dataPartida == null){
                textViewDatas.text = textViewDatas.text.toString() + "Não Definido!"
            }else {
                textViewDatas.text = textViewDatas.text.toString() + Utils.formatarData(viagem.dataPartida!!)
            }
            var total = viagem.calculaGastos()
            textViewGastos.text = "Total gastos R$: ${Utils.formatarValor(total)}"
            progressBarOrcamento.max = viagem.orcamento.toInt()
            progressBarOrcamento.progress = total.toInt()
            var porcentagem = viagem.calculaPorcetagemGasto()
            textPorcetagem.text = Utils.formatarValor(porcentagem) + "%"

            if (viagem.tipoViagem == 0) {
                imageViewTipo.setImageResource(R.drawable.ferias)
            }
            else
            {
                imageViewTipo.setImageResource(R.drawable.negocio)
            }

            selecionaCorProgressBar(porcentagem, progressBarOrcamento)
        }

        private fun selecionaCorProgressBar(porcentagem : Double, progressBar: ProgressBar){
            when {
                porcentagem > 0 && porcentagem < 50 -> {
                    progressBar.progressDrawable.setColorFilter(
                        Color.rgb(0,128,0), android.graphics.PorterDuff.Mode.SRC_IN)
                }
                porcentagem >= 50 && porcentagem < 70 -> {
                    progressBar.progressDrawable.setColorFilter(
                        Color.rgb(173,255,47), android.graphics.PorterDuff.Mode.SRC_IN)
                }
                porcentagem > 70 && porcentagem < 90 -> {
                    progressBar.progressDrawable.setColorFilter(
                        Color.rgb(240,128,128), android.graphics.PorterDuff.Mode.SRC_IN)
                }

                porcentagem >= 90 -> {
                    progressBar.progressDrawable.setColorFilter(
                        Color.rgb(240,0,0), android.graphics.PorterDuff.Mode.SRC_IN)
                }
            }
        }

        override fun onCreateContextMenu(
            menu: ContextMenu?,
            v: View?,
            menuInfo: ContextMenu.ContextMenuInfo?
        ) {
            menu?.setHeaderTitle("Opções")
            val editar = menu?.add("Editar")
            val novoGasto = menu?.add("Novo Gasto")
            val gastosRealizados = menu?.add("Gastos realizados")
            val excluir = menu?.add("Excluir")

            editar?.setOnMenuItemClickListener {
                onEditar?.invoke(viagens[adapterPosition]) ?: false
            }
            novoGasto?.setOnMenuItemClickListener {
                onNovoGasto?.invoke(viagens[adapterPosition]) ?: false
            }
            gastosRealizados?.setOnMenuItemClickListener {
                onGastosRealizados?.invoke(viagens[adapterPosition]) ?: false
            }
            excluir?.setOnMenuItemClickListener {
                onExcluir?.invoke(viagens[adapterPosition]) ?: false
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_layout_viagens_recycler_view, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return this.viagens.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(viagens[position])
    }
}