package com.example.boaviagem.view

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.example.boaviagem.R
import com.example.boaviagem.model.Usuario

class MainActivity : AppCompatActivity() {

    private var usuarioLogado: Usuario? = null

    private lateinit var textViewUsuarioLogado: TextView
    private lateinit var ImageViewNovoGasto: ImageView
    private lateinit var ImageViewNovaViagem: ImageView
    private lateinit var ImageViewMinhasViagens: ImageView

    val RETORNO_AUTENTICACAO = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        ImageViewNovoGasto = findViewById<ImageView>(R.id.iv_NovoGasto)
        ImageViewNovoGasto.setOnClickListener {
            val i = Intent(this, NovoGastoActivity::class.java)
            i.putExtra("usuario", usuarioLogado)
            startActivity(i)
        }

        ImageViewNovaViagem = findViewById<ImageView>(R.id.iv_NovaViagem)
        ImageViewNovaViagem.setOnClickListener {
            val i = Intent(this, NovaViagemActivity::class.java)
            i.putExtra("usuario", usuarioLogado)
            startActivity(i)
        }

        ImageViewMinhasViagens = findViewById<ImageView>(R.id.iv_MinhasViagens)
        ImageViewMinhasViagens.setOnClickListener {
            val i = Intent(this, ListaViagensActivity::class.java)
            i.putExtra("usuario", usuarioLogado)
            startActivity(i)
        }

        textViewUsuarioLogado = findViewById<TextView>(R.id.tv_UsuarioLogado)
        textViewUsuarioLogado.setOnClickListener {
            resetaLogin()
        }

        recuperaDadosIntent()
    }

    private fun resetaLogin() {
        val alertDialog: AlertDialog? = this@MainActivity?.let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setPositiveButton("Sim",
                    DialogInterface.OnClickListener { dialog, id ->
                        usuarioLogado = null
                        abreLogin()
                    })
                setNegativeButton("Não",
                    DialogInterface.OnClickListener { dialog, id ->
                    })
            }
            builder.setMessage("Deseja trocar de usuário ?")
            builder.create()
            builder.show()
        }
    }

    private fun recuperaDadosIntent() {
        var bundle: Bundle? = intent.extras
        if (bundle != null) {
            usuarioLogado = bundle.getSerializable("usuario") as Usuario
        }

        if ((bundle == null) || (usuarioLogado == null)) {
            abreLogin()
        }
    }

    private fun abreLogin() {
        val i = Intent(this, LoginActivity::class.java)
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(i, RETORNO_AUTENTICACAO)
    }

    private fun setUsuarioLogadoView() {
        if (usuarioLogado != null) {
            textViewUsuarioLogado.text =
                usuarioLogado!!.idUsuario.toString() + " - " + usuarioLogado!!.nome
        }
        else
        {
            textViewUsuarioLogado.text = "Usuário não autenticado!!!"
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if ((requestCode == RETORNO_AUTENTICACAO) && resultCode == RESULT_OK) {
            if (data != null) {
                usuarioLogado = data.getSerializableExtra("usuario") as Usuario
                setUsuarioLogadoView()
            }
        }
    }
}
