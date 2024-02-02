package co.tiagoaguiar.evernotekt.home

import co.tiagoaguiar.evernotekt.model.Note

interface Home {

    // controller, lógica fica aqui. devolve informações pra view e pede informações pro model
    interface Presenter {
        fun getAllNotes()
        fun stop()
    }

    // implementar uma Activity ou Fragment
    interface View {
        fun displayNotes(notes: List<Note>)
        fun displayEmptyNotes()
        fun displayError(message: String)
    }
}