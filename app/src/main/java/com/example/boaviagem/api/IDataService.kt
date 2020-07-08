package com.example.boaviagem.api

import com.example.boaviagem.model.Gasto
import com.example.boaviagem.model.Usuario
import com.example.boaviagem.model.Viagem
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*
import java.util.*

interface IDataService {

    @GET("viagem")
    fun findAllViagens(): Call<List<Viagem>>

    @GET("viagem/ViagensDoUsuario/{idusuario}")
    fun ViagensDoUsuario(@Path("idusuario") idusuario: Int): Call<List<Viagem>>

    @POST("viagem/ValidaPeriodoViagem/{idviagem}")
    fun ValidaPeriodoViagem(@Path("idviagem") idviagem: Int, @Body data: String): Call<Viagem>

    @GET("usuario/{id}")
    fun findUsuario(@Path("id") id: Int): Call<Usuario>

    @POST("usuario/autentica")
    fun validaAutenticacao(@Body login: Usuario): Call<Usuario>

    @POST("viagem/salvaViagem")
    fun postViagem(@Body viagem: Viagem): Call<Viagem>

    @DELETE("viagem/{id}")
    fun deleteViagem(@Path("id") id: Int): Call<Void>

    @POST("viagem/adicionaGasto/{idviagem}")
    fun postGasto(@Path("idviagem") idviagem: Int, @Body gasto: Gasto) : Call<Viagem>

    @POST("viagem/deletaGasto/{idviagem}")
    fun deletaGasto(@Path("idviagem") idviagem: Int, @Body gasto: Gasto): Call<Void>
}