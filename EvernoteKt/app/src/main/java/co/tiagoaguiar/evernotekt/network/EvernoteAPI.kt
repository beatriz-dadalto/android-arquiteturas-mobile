package co.tiagoaguiar.evernotekt.network

import co.tiagoaguiar.evernotekt.model.Note
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

/**
 *
 * Setembro, 24 2019
 * @author suporte@moonjava.com.br (Tiago Aguiar).
 */

interface EvernoteAPI {

    @GET("/")
    fun listNotes(): Call<List<Note>>

    @GET("/{id}")
    fun getNote(@Path("id") id: Int): Call<Note>

    @POST("/create")
    fun createNote(@Body note: Note): Call<Note>

}