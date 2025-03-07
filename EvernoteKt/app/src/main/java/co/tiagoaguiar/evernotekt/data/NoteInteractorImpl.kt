package co.tiagoaguiar.evernotekt.data

import co.tiagoaguiar.evernotekt.data.model.Note
import co.tiagoaguiar.evernotekt.data.model.NoteState
import co.tiagoaguiar.evernotekt.data.model.RemoteDataSource
import io.reactivex.Observable

class NoteInteractorImpl : IInteractor {
    private val remoteDataSource = RemoteDataSource()

    override fun createNote(note: Note): Observable<NoteState> {
        return remoteDataSource.createNote(note)
            .map<NoteState> { NoteState.FinishState }
            .onErrorReturn { NoteState.ErrorState("Error") }
    }

    override fun getNote(id: Int): Observable<NoteState> {
        return remoteDataSource.getNote(id)
            .map<NoteState> { NoteState.SingleDataState(it) }
            .onErrorReturn { NoteState.ErrorState("Error") }
    }

    override fun getAllNotes(): Observable<NoteState> {
        return remoteDataSource.listNotes()
            .map<NoteState> { NoteState.DataState(it) }
            .onErrorReturn { NoteState.ErrorState("Error") }
    }
}