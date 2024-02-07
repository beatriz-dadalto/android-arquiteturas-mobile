package co.tiagoaguiar.evernotekt.data

import androidx.lifecycle.LiveData
import co.tiagoaguiar.evernotekt.data.model.Note

interface INoteRepository {

    fun getAllNotes(): LiveData<List<Note>?>

    fun getNote(noteId: Int): LiveData<Note?>

    fun createNote(note: Note): LiveData<Note>

}