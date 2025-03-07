package co.tiagoaguiar.evernotekt.data

import co.tiagoaguiar.evernotekt.data.model.Note
import co.tiagoaguiar.evernotekt.data.model.NoteState
import io.reactivex.Observable

interface IInteractor {
    // sempre retorna o estado que o app está no momento
    fun createNote(note: Note): Observable<NoteState>
    fun getNote(id: Int): Observable<NoteState>
    fun getAllNotes(): Observable<NoteState>
}