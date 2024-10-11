package co.tiagoaguiar.evernotekt.interactor

import androidx.lifecycle.MediatorLiveData
import co.tiagoaguiar.evernotekt.IHome
import co.tiagoaguiar.evernotekt.data.NoteRepositoryImpl
import co.tiagoaguiar.evernotekt.data.model.Note

class HomeInteractor : IHome.Interactor {

    private val noteList = MediatorLiveData<List<Note>?>()
    private val repository = NoteRepositoryImpl()

    init {
        getAllNotes()
    }

    override fun getAllNotes() {
        noteList.addSource(repository.getAllNotes()) { notes ->
            noteList.postValue(notes)
        }
    }

    override fun loadNoteList() = noteList
}