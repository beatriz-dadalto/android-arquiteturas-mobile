package co.tiagoaguiar.evernotekt.network

import co.tiagoaguiar.evernotekt.model.Note
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface EvernoteAPI {

    @GET("/")
    fun listNotes(): Observable<List<Note>>

    @GET("/{id}")
    fun getNote(@Path("id") id: Int): Observable<Note>

    @POST("/create")
    fun createNote(@Body note: Note): Observable<Note>

}