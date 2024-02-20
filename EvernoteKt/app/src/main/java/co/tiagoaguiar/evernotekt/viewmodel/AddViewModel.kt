package co.tiagoaguiar.evernotekt.viewmodel

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import co.tiagoaguiar.evernotekt.data.INoteRepository
import co.tiagoaguiar.evernotekt.data.NoteRepositoryImpl
import co.tiagoaguiar.evernotekt.data.model.Note

class AddViewModel(
    private val repository: INoteRepository = NoteRepositoryImpl()
) : ViewModel() {

    private val saveLiveData = MutableLiveData<Boolean>()

    /*
        na Activity terá uma referencia para essa variavel evitando que uma parte do projeto
        modifique o MutableLiveData desse viewModel
     */
    val saved: LiveData<Boolean>
        get() = saveLiveData

    var title = ObservableField("")
    var body = ObservableField("")

    fun createNote() {
        // validar
        if (title.get().isNullOrEmpty() || body.get().isNullOrEmpty()) {
            saveLiveData.value = false
            return
        }

        val note = Note(title = title.get(), body = body.get())
        repository.createNote(note)
        saveLiveData.value = true
    }
}