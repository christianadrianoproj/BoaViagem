package com.example.boaviagem.view

import android.app.Activity
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import com.example.boaviagem.R
import com.example.boaviagem.config.RetrofitInitializer
import com.example.boaviagem.model.Usuario
import com.example.boaviagem.model.Viagem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class LoginActivity : AppCompatActivity() {

    private lateinit var username: EditText
    private lateinit var password: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        username = findViewById<EditText>(R.id.et_usuario)
        password = findViewById<EditText>(R.id.et_senha)
        val btn = findViewById<Button>(R.id.bt_entrar)
        btn.setOnClickListener {
            if (validate()) {
                PostAutenticaUsuario()
            }
        }
    }

    private fun validate() : Boolean {
        var result = true
        if (username.getText().trim().length == 0) {
            username.setError("Campo é obrigatório")
            result = false
        }
        if (password.getText().trim().length == 0) {
            password.setError("Campo é obrigatório")
            result = false
        }
        return result
    }

    private fun validaAutenticacao(usuario: Usuario) {
        if ((usuario != null) && (usuario.idUsuario != null) && (usuario.idUsuario!! > 0)) {
            this.intent.putExtra("usuario", usuario)
            this.setResult(Activity.RESULT_OK, intent)
            this.finish()
        }
        else {
            AlertDialog.Builder(this)
                .setTitle("Autenticação")
                .setMessage("Usuário e/ou Senha Incorreta!!!")
                .setPositiveButton("OK") { dialog, which ->
                    dialog.dismiss()
                }.show()
        }
    }

    private fun PostAutenticaUsuario() {
        var usuario = Usuario(null, "", username.text.toString(), password.text.toString())
        var call = RetrofitInitializer().Service().validaAutenticacao(usuario)
        call.enqueue(object : Callback<Usuario> {
            override fun onFailure(call: Call<Usuario>, t: Throwable) {
                Log.d("CHRISTIAN", "Error : ${t.message}")
                Toast.makeText(
                    this@LoginActivity,
                    "Ocorreu um erro tente novamente",
                    Toast.LENGTH_LONG
                ).show()
            }
            override fun onResponse(call: Call<Usuario>, response: Response<Usuario>) {
                if (response.isSuccessful) {
                    usuario  = response.body()!!
                    validaAutenticacao(usuario)
                }
            }
        })
    }

    override fun onBackPressed() {
        // Não é permitido voltar para activity anterior...
    }
}
