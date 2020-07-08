package com.example.boaviagem.model

import java.io.Serializable

data class Usuario(val idUsuario: Int?,
                 val nome: String,
                 val userName: String,
                 val password: String
) : Serializable {

    constructor(): this(null, "", "", "")
}