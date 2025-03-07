package co.tiagoaguiar.evernotekt.view

import co.tiagoaguiar.evernotekt.data.model.NoteState
import io.reactivex.Observable

interface IHomeView {
    fun render(state: NoteState)
    fun displayNotesIntent(): Observable<Unit>
}