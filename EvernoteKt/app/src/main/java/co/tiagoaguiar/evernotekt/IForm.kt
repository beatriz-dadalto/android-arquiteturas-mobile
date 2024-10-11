package co.tiagoaguiar.evernotekt

import androidx.lifecycle.LiveData
import co.tiagoaguiar.evernotekt.data.model.Note

interface IForm {

    interface View {
        fun displayError(message: String)
        fun displayNote(note: Note)
    }

    interface Presenter {
        fun onStart(noteId: Int?)
        fun backPressClick()
        fun saveNote(title: String, body: String)
    }

    // Interactor se comunica com o backend, local storage, API...
    interface Interactor {
        fun createNote(note: Note)
        fun saved(): LiveData<Boolean>

        fun getNote(id: Int)
        fun loadNote(): LiveData<Note?>
    }

    interface InteractorOutput {
        fun onSaveSuccess()
        fun onSaveError()

        fun onQuerySuccess(data: Note)
        fun onQueryError()
    }
}