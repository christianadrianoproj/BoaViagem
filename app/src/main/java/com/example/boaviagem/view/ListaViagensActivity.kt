package com.example.boaviagem.view

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.boaviagem.R
import com.example.boaviagem.adapter.AdapterViagens
import com.example.boaviagem.config.RetrofitInitializer
import com.example.boaviagem.model.Usuario
import com.example.boaviagem.model.Viagem
import com.example.boaviagem.util.RecyclerItemClickListener
import com.example.boaviagem.util.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class ListaViagensActivity : AppCompatActivity() {

    private var usuarioLogado: Usuario? = null
    private lateinit var recyclerViewViagens : RecyclerView
    private lateinit var adapter: AdapterViagens
    private var listaViagens: ArrayList<Viagem> = mutableListOf<Viagem>() as ArrayList<Viagem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_viagens)
        recyclerViewViagens = findViewById(R.id.recyclerViewViagens)

        recuperaUsuarioLogado()
        onClickRecyclerView()
    }

    override fun onStart() {
        super.onStart()
        CarregaViagens()
    }

    private fun recuperaUsuarioLogado() {
        var bundle: Bundle? = intent.extras
        if (bundle != null) {
            usuarioLogado = bundle.getSerializable("usuario") as Usuario
        }
    }

    private fun CarregaViagens() {
        var call = RetrofitInitializer().Service().ViagensDoUsuario(usuarioLogado!!.idUsuario!!)
        call.enqueue(object : Callback<List<Viagem>> {
            override fun onFailure(call: Call<List<Viagem>>, t: Throwable) { Log.d("CHRISTIAN", t.message) }
            override fun onResponse(call: Call<List<Viagem>>, response: Response<List<Viagem>>) {
                Log.d("CHRISTIAN", response.toString())
                if (response.isSuccessful) {
                    listaViagens = response.body()!! as ArrayList<Viagem>
                    configuraAdapter(listaViagens)
                    adapter.notifyDataSetChanged()
                }
            }
        })
    }

    private fun configuraAdapter(list: List<Viagem>) {
        adapter = AdapterViagens(list as MutableList<Viagem>)

        adapter.onEditar = {
            val i = Intent(this, NovaViagemActivity::class.java)
            i.putExtra("usuario", usuarioLogado)
            i.putExtra("viagem", it)
            startActivity(i)
            true
        }

        adapter.onNovoGasto = {
            val i = Intent(this, NovoGastoActivity::class.java)
            i.putExtra("usuario", usuarioLogado)
            startActivity(i)
            true
        }

        adapter.onGastosRealizados = {
            startActivity(
                Intent(this@ListaViagensActivity, ListaGastosActivity::class.java)
                    .putExtra("usuario", usuarioLogado)
                    .putExtra("viagem", it)
            )
            true
        }

        adapter.onExcluir = {
            AlertDialog.Builder(this)
                .setMessage("Deseja excluir a viagem ${it.destino}?")
                .setPositiveButton("Sim", DialogInterface.OnClickListener { _, _ ->
                    deletaViagem(it)
                    true
                })
                .setNegativeButton("Não", DialogInterface.OnClickListener { _, _ ->
                    false
                })
                .create().show()
            true
        }

        configuraRecyclerViewViagens(adapter)
    }


    private fun configuraRecyclerViewViagens(adapter: AdapterViagens) {
        val layout = LinearLayoutManager(this)
        this.adapter = adapter
        recyclerViewViagens.adapter = adapter
        recyclerViewViagens.layoutManager = layout
        recyclerViewViagens.addItemDecoration(
            DividerItemDecoration(
                this,
                LinearLayout.VERTICAL
            )
        )
        recyclerViewViagens.setHasFixedSize(true)
    }

    private fun onClickRecyclerView() {
        recyclerViewViagens.addOnItemTouchListener(
            RecyclerItemClickListener(this,
                recyclerViewViagens,
                object : RecyclerItemClickListener.OnItemClickListener {
                    override fun onLongItemClick(view: View?, position: Int) {

                    }

                    override fun onItemClick(view: View?, position: Int) {
                        val viagem = listaViagens[position]
                        startActivity(
                            Intent(this@ListaViagensActivity, ListaGastosActivity::class.java)
                                .putExtra("usuario", usuarioLogado)
                                .putExtra("viagem", viagem)
                        )
                    }
                    override fun onItemClick(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {

                    }

                })
        )
    }

    private fun deletaViagem(viagem: Viagem) {
        val call = RetrofitInitializer().Service().deleteViagem(viagem!!.idViagem!!)
        call.enqueue(object : Callback<Void> {
            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.d("CHRISTIAN", t.message)
                Toast.makeText(
                    this@ListaViagensActivity,
                    "Ocorreu um erro tente novamente",
                    Toast.LENGTH_LONG
                ).show()
            }
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                Log.d("CHRISTIAN", response.toString())
                if (response.isSuccessful) {
                    Toast.makeText(
                        this@ListaViagensActivity,
                        "Viagem removida com sucesso!",
                        Toast.LENGTH_LONG
                    ).show()
                    CarregaViagens()
                }
            }
        })
    }
}