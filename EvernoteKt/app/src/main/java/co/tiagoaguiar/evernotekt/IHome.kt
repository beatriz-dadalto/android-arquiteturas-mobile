package co.tiagoaguiar.evernotekt

import androidx.lifecycle.LiveData
import co.tiagoaguiar.evernotekt.data.model.Note

interface IHome {

    interface View {
        fun displayNotes(notes: List<Note>)
        fun displayError(message: String)
    }

    interface Presenter {
        fun onStart()
        fun addNoteClick() // qual rota vai direcionar quando clicar para salvar nota
    }

    interface Interactor {
        fun getAllNotes()
        fun loadNoteList(): LiveData<List<Note>?>
    }

    interface InteractorOutput {
        fun onQuerySuccess(data: List<Note>)
        fun onQueryError()
    }
}