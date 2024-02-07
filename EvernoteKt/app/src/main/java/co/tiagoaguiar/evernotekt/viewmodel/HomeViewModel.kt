package co.tiagoaguiar.evernotekt.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import co.tiagoaguiar.evernotekt.data.INoteRepository
import co.tiagoaguiar.evernotekt.data.NoteRepositoryImpl
import co.tiagoaguiar.evernotekt.data.model.Note

class HomeViewModel(
    private val repository: INoteRepository = NoteRepositoryImpl()
): ViewModel() {

    // quem tiver a instancia de um obj HomeViewModel vai receber o LiveData do NoteRepositoryImpl

    fun getAllNotes(): LiveData<List<Note>?> {
        return repository.getAllNotes()
    }
}