package co.tiagoaguiar.evernotekt.presenter

import androidx.lifecycle.Observer
import co.tiagoaguiar.evernotekt.IForm
import co.tiagoaguiar.evernotekt.data.model.Note
import co.tiagoaguiar.evernotekt.view.activities.FormActivity
import ru.terrakok.cicerone.Router

class FormPresenter(
    private var view: IForm.View?,
    private var interactor: IForm.Interactor?,
    private val router: Router?
) : IForm.Presenter, IForm.InteractorOutput {

    override fun onStart(noteId: Int?) {
        interactor?.saved()?.observe((view as FormActivity), Observer { saved ->
            if (saved) {
                onSaveSuccess()
            } else {
                onSaveError()
            }
        })

        interactor?.loadNote()?.observe((view as FormActivity), Observer { response ->
            if (response != null) {
                onQuerySuccess(response)
            } else {
                onQueryError()
            }
        })

        noteId?.let { interactor?.getNote(it) }
    }

    override fun backPressClick() {
        router?.exit()
    }

    override fun saveNote(title: String, body: String) {
        if (title.isEmpty() || body.isEmpty()) {
            view?.displayError("Título e nota deve ser informado")
            return
        }

        interactor?.createNote(Note(title = title, body = body))
    }

    override fun onSaveSuccess() {
        router?.exit()
    }

    override fun onSaveError() {
        view?.displayError("Erro ao salvar nota")
    }

    override fun onQuerySuccess(data: Note) {
        view?.displayNote(data)
    }

    override fun onQueryError() {
        view?.displayError("Erro ao carregar dados")
    }
}