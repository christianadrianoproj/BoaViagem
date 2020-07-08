package com.example.boaviagem.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.example.boaviagem.R
import com.example.boaviagem.config.RetrofitInitializer
import com.example.boaviagem.model.Gasto
import com.example.boaviagem.model.Usuario
import com.example.boaviagem.model.Viagem
import com.example.boaviagem.util.Utils
import kotlinx.android.synthetic.main.activity_nova_viagem.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class NovoGastoActivity : AppCompatActivity() {
    private var usuarioLogado: Usuario? = null

    private lateinit var spinnerTipoGasto: Spinner
    private lateinit var spinnerViagem: Spinner

    private lateinit var textViewLocal: TextView
    private lateinit var textViewData: TextView
    private lateinit var textViewValor: TextView
    private lateinit var textViewDescricao: TextView

    private lateinit var viagens: List<Viagem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_novo_gasto)

        recuperaUsuarioLogado()

        textViewValor = findViewById<TextView>(R.id.et_valor)
        textViewData = findViewById<TextView>(R.id.et_data)

        val botao = findViewById<Button>(R.id.buttonCadastrarGasto)
        botao.setOnClickListener {
            cadastraNovoGastom()
        }

        textViewDescricao = findViewById<TextView>(R.id.et_descricao)
        textViewLocal = findViewById<TextView>(R.id.et_local)

        spinnerViagem = findViewById<Spinner>(R.id.spViagem)
        spinnerTipoGasto = findViewById<Spinner>(R.id.spTipoGasto)
        ArrayAdapter.createFromResource(
            this,
            R.array.lista_TiposGastos,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerTipoGasto.adapter = adapter
        }

        preencheViagens()
    }

    private fun recuperaUsuarioLogado() {
        var bundle: Bundle? = intent.extras
        if (bundle != null) {
            usuarioLogado = bundle.getSerializable("usuario") as Usuario
        }
    }

    private fun configAdapterViagens(viagens: List<Viagem>): ArrayAdapter<Viagem> {
        var adapter: ArrayAdapter<Viagem> =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, viagens)
        return adapter
    }

    private fun preencheViagens() {
        var call = RetrofitInitializer().Service().ViagensDoUsuario(usuarioLogado!!.idUsuario!!)
        call.enqueue(object : Callback<List<Viagem>> {
            override fun onFailure(call: Call<List<Viagem>>, t: Throwable) { }
            override fun onResponse(call: Call<List<Viagem>>, response: Response<List<Viagem>>) {
                if (response.isSuccessful) {
                    viagens = response.body()!!
                    spinnerViagem.adapter = viagens?.let { configAdapterViagens(it) }
                }
            }
        })
    }

    private fun validaDataGastoComViagem() {
        val viagem = spinnerViagem.getItemAtPosition(spinnerViagem.selectedItemId.toInt()) as Viagem
        var call = RetrofitInitializer().Service().ValidaPeriodoViagem(viagem.idViagem!!, textViewData.text.toString())
        call.enqueue(object : Callback<Viagem> {
            override fun onFailure(call: Call<Viagem>, t: Throwable) { Log.d("CHRISTIAN", t.message) }
            override fun onResponse(call: Call<Viagem>, response: Response<Viagem>) {
                Log.d("CHRISTIAN", response.toString())
                if (response.isSuccessful) {
                    val viagem = response.body()!!
                    if ((viagem != null) && (viagem!!.idViagem != null) && (viagem!!.idViagem!! > 0)) {
                        postGasto()
                    }
                    else {
                        AlertDialog.Builder(this@NovoGastoActivity)
                            .setTitle("Validação")
                            .setMessage("Data informada não corresponde ao período da viagem selecionada!")
                            .setPositiveButton("OK") { dialog, which ->
                                dialog.dismiss()
                            }.show()
                    }
                }
            }
        })
    }

    private fun validate() : Boolean {
        var result = true
        if ((result) && (spinnerTipoGasto.selectedItemId.toInt() == -1)) {
            AlertDialog.Builder(this)
                .setTitle("Validação")
                .setMessage("Selecione o Tipo de Gasto!")
                .setPositiveButton("OK") { dialog, which -> dialog.dismiss()
                }.show()
            result = false
        }
        if ((result) && ((textViewValor.getText().trim().isEmpty()) || (textViewValor.text.toString().toDouble() <= 0) ) ) {
            textViewValor.setError("Campo é obrigatório")
            result = false
        }
        if ((result) && (textViewData.getText().trim().isEmpty())) {
            textViewData.setError("Campo é obrigatório")
            result = false
        }
        if ((result) && (textViewDescricao.getText().trim().isEmpty()))  {
            textViewDescricao.setError("Campo é obrigatório")
            result = false
        }
        if ((result) &&  (textViewLocal.getText().trim().isEmpty())) {
            textViewLocal.setError("Campo é obrigatório")
            result = false
        }
        if ((result) && (spinnerViagem.selectedItemId.toInt() == -1)) {
            AlertDialog.Builder(this)
                .setTitle("Validação")
                .setMessage("Selecione a Viagem para ser vinculado o Gasto!")
                .setPositiveButton("OK") { dialog, which -> dialog.dismiss()
                }.show()
            result = false
        }
        return result
    }

    private fun cadastraNovoGastom() {
        if (validate()) {
            validaDataGastoComViagem()
        }
    }

    private fun postGasto() {
        var data: Date? = Utils.DataStrToDate(textViewData.text.toString())
        val viagem = spinnerViagem.getItemAtPosition(spinnerViagem.selectedItemId.toInt()) as Viagem
        val listGastos = resources.getStringArray(R.array.lista_TiposGastos)
        val tipoDeGasto: String = listGastos[spinnerTipoGasto.selectedItemId.toInt()]
        val gasto = Gasto(null,
            viagem,
            tipoDeGasto,
            textViewValor.text.toString().toDouble(), data,
            textViewDescricao.text.toString(), textViewLocal.text.toString())
        val call = RetrofitInitializer().Service().postGasto(viagem!!.idViagem!!, gasto)
        call.enqueue(object : Callback<Viagem> {
            override fun onFailure(call: Call<Viagem>, t: Throwable) {
                Log.d("CHRISTIAN", t.message)
                Toast.makeText(
                    this@NovoGastoActivity,
                    "Ocorreu um erro tente novamente",
                    Toast.LENGTH_LONG
                ).show()
            }
            override fun onResponse(call: Call<Viagem>, response: Response<Viagem>) {
                Log.d("CHRISTIAN", response.toString())
                if (response.isSuccessful) {
                    Toast.makeText(
                        this@NovoGastoActivity,
                        "Gasto lançado com sucesso!",
                        Toast.LENGTH_LONG
                    ).show()
                    this@NovoGastoActivity.finish()
                }
            }
        })
    }
}