package co.tiagoaguiar.evernotekt.model

import co.tiagoaguiar.evernotekt.network.RetrofitClient
import retrofit2.Callback

/**
 *
 * Setembro, 24 2019
 * @author suporte@moonjava.com.br (Tiago Aguiar).
 */
class RemoteDataSource {

    fun listNotes(callback: Callback<List<Note>>) {
        RetrofitClient.evernoteApi
            .listNotes()
            .enqueue(callback)
    }

    fun getNote(noteId: Int, callback: Callback<Note>) {
        RetrofitClient.evernoteApi
            .getNote(noteId)
            .enqueue(callback)
    }

    fun createNote(note: Note, callback: Callback<Note>) {
        RetrofitClient.evernoteApi
            .createNote(note)
            .enqueue(callback)
    }

}