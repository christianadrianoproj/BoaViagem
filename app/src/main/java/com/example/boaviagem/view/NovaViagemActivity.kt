package com.example.boaviagem.view

import android.icu.text.SimpleDateFormat
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import com.example.boaviagem.R
import com.example.boaviagem.config.RetrofitInitializer
import com.example.boaviagem.model.Usuario
import com.example.boaviagem.model.Viagem
import com.example.boaviagem.util.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class NovaViagemActivity : AppCompatActivity() {

    private var usuarioLogado: Usuario? = null
    private var viagem: Viagem = Viagem()

    private lateinit var textViewDestino: TextView
    private lateinit var rgtipoViagem: RadioGroup
    private lateinit var textViewDataChegada: TextView
    private lateinit var textViewDataPartida: TextView
    private lateinit var textViewOrcamento: TextView
    private lateinit var textViewQtdPessoas: TextView
    private lateinit var botao: Button

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nova_viagem)

        textViewDestino = findViewById<TextView>(R.id.et_destino)
        rgtipoViagem = findViewById<RadioGroup>(R.id.rg_tipoViagem)
        textViewDataChegada = findViewById<TextView>(R.id.et_dataChegada)
        textViewDataPartida = findViewById<TextView>(R.id.et_dataPartida)
        textViewOrcamento = findViewById<TextView>(R.id.et_orcamento)
        textViewQtdPessoas = findViewById<TextView>(R.id.et_quantidadePessoas)
        botao = findViewById<Button>(R.id.buttonCadastrar)

        val botao = findViewById<Button>(R.id.buttonCadastrar)
        botao.setOnClickListener {
            cadastraNovaViagem()
        }
        recuperaUsuarioLogado()
    }

    private fun recuperaUsuarioLogado() {
        var bundle: Bundle? = intent.extras
        if (bundle != null) {
            usuarioLogado = bundle.getSerializable("usuario") as Usuario
            if (bundle.getSerializable("viagem") != null) {
                viagem = bundle.getSerializable("viagem") as Viagem
                botao.text = "Editar Viagem"
                preencheCampos()
            }
        }
    }

    private fun preencheCampos() {
        if (viagem.idViagem != null) {
            textViewDestino.text = viagem.destino
            textViewDataChegada.text = Utils.formatarData(viagem.dataChegada!!)
            textViewOrcamento.text = viagem.orcamento.toString()
            textViewQtdPessoas.text = viagem.quantidadePessoas.toString()
            textViewDataPartida.text = Utils.formatarData(viagem.dataPartida!!)
            rgtipoViagem.check(if (viagem.tipoViagem == 0) R.id.rb_Lazer else R.id.rb_Negocios)
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun validate() : Boolean {
        var result = true
        if (textViewDestino.getText().trim().isEmpty()) {
            textViewDestino.setError("Campo é obrigatório")
            result = false
        }
        if ((result) && (textViewDataChegada.getText().trim().isEmpty())) {
            textViewDataChegada.setError("Campo é obrigatório")
            result = false
        }
        if ((result) && (textViewOrcamento.getText().trim().isEmpty())) {
            textViewOrcamento.setError("Campo é obrigatório")
            result = false
        }
        if ((result) && ((textViewQtdPessoas.getText().trim().isEmpty()) || (textViewQtdPessoas.text.toString().toInt() < 1) ) ) {
            textViewQtdPessoas.setError("Campo é obrigatório")
            result = false
        }
        if ((result) && (rgtipoViagem.checkedRadioButtonId == -1)) {
            AlertDialog.Builder(this)
                .setTitle("Validação")
                .setMessage("Selecione o Tipo de Viagem!")
                .setPositiveButton("OK") { dialog, which -> dialog.dismiss()
                }.show()
            result = false
        }
        if ((result) && (!textViewDataPartida.getText().trim().isEmpty())) {
            val dataChegada: Date = SimpleDateFormat("dd/MM/yyyy").parse(textViewDataChegada.text.toString())
            val dataPartida: Date = SimpleDateFormat("dd/MM/yyyy").parse(textViewDataPartida.text.toString())
            if (dataPartida.before(dataChegada)) {
                textViewDataPartida.setError("Data partida não pode ser inferior a data de chegada")
                result = false
            }
        }
        return result
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun cadastraNovaViagem() {
        if (validate()) {
            postViagem()
        }
    }

    private fun postViagem() {
        val tipoViagem = if (rgtipoViagem.checkedRadioButtonId == R.id.rb_Lazer) 0 else 1

        var dataPartida: Date? = null
        if (!textViewDataPartida.text.isEmpty()) {
            dataPartida = Utils.DataStrToDate(textViewDataPartida.text.toString())
        }

        viagem!!.destino = textViewDestino.text.toString()
        viagem!!.dataChegada = Utils.DataStrToDate(textViewDataChegada.text.toString())
        viagem!!.dataPartida = dataPartida
        viagem!!.orcamento = textViewOrcamento.text.toString().toDouble()
        viagem!!.quantidadePessoas = textViewQtdPessoas.text.toString().toInt()
        viagem!!.usuario = usuarioLogado
        viagem!!.tipoViagem = tipoViagem

        val call = RetrofitInitializer().Service().postViagem(viagem!!)
        call.enqueue(object : Callback<Viagem> {
            override fun onFailure(call: Call<Viagem>, t: Throwable) {
                Log.d("CHRISTIAN", t.message)
                Toast.makeText(
                    this@NovaViagemActivity,
                    "Ocorreu um erro tente novamente",
                    Toast.LENGTH_LONG
                ).show()
            }
            override fun onResponse(call: Call<Viagem>, response: Response<Viagem>) {
                Log.d("CHRISTIAN", response.toString())
                if (response.isSuccessful) {
                    Toast.makeText(
                        this@NovaViagemActivity,
                        if ((viagem.idViagem != null) && (viagem.idViagem!! > 0))  "Viagem Editada com sucesso!" else "Viagem criada com sucesso!",
                        Toast.LENGTH_LONG
                    ).show()
                    this@NovaViagemActivity.finish()
                }
            }
        })
    }
}