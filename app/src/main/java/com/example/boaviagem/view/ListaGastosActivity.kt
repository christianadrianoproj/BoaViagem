package com.example.boaviagem.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.boaviagem.R
import com.example.boaviagem.adapter.AdapterGastosPorData
import com.example.boaviagem.config.RetrofitInitializer
import com.example.boaviagem.model.DataGasto
import com.example.boaviagem.model.Gasto
import com.example.boaviagem.model.Usuario
import com.example.boaviagem.model.Viagem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListaGastosActivity : AppCompatActivity() {

    private var usuarioLogado: Usuario? = null
    private lateinit var viagem: Viagem
    private lateinit var recyclerViewGastos: RecyclerView
    private lateinit var adapter: AdapterGastosPorData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_gastos)

        recyclerViewGastos = findViewById(R.id.recyclerViewGastos)
        recuperaUsuarioLogado()
    }

    override fun onStart() {
        super.onStart()
        CarregaGastos()
    }

    private fun recuperaUsuarioLogado() {
        var bundle: Bundle? = intent.extras
        if (bundle != null) {
            usuarioLogado = bundle.getSerializable("usuario") as Usuario
            viagem = bundle.getSerializable("viagem") as Viagem
        }
    }

    private fun CarregaGastos() {
        configuraAdapter(viagem.getDataGasto())
        adapter.notifyDataSetChanged()
    }

    private fun configuraAdapter(list: MutableList<DataGasto>?) {
        adapter = AdapterGastosPorData(list as MutableList<DataGasto>)
        configuraRecyclerViewGastos(adapter)
    }

    private fun configuraRecyclerViewGastos(adapter: AdapterGastosPorData) {
        this.adapter = adapter
        recyclerViewGastos.adapter = adapter
        recyclerViewGastos.addItemDecoration(
            DividerItemDecoration(
                this,
                LinearLayout.VERTICAL
            )
        )
    }

}