package co.tiagoaguiar.evernotekt.presenter

import androidx.lifecycle.Observer
import co.tiagoaguiar.evernotekt.IHome
import co.tiagoaguiar.evernotekt.data.model.Note
import co.tiagoaguiar.evernotekt.view.activities.FormActivity
import co.tiagoaguiar.evernotekt.view.activities.HomeActivity
import ru.terrakok.cicerone.Router

class HomePresenter(
    private var view: IHome.View?,
    private var interactor: IHome.Interactor?,
    private val router: Router?
): IHome.Presenter, IHome.InteractorOutput {

    override fun onStart() {
        interactor?.loadNoteList()?.observe((view as HomeActivity), Observer {response ->
            if (response != null) {
                onQuerySuccess(response)
            } else {
                onQueryError()
            }
        })
    }

    override fun addNoteClick() {
        router?.navigateTo(FormActivity.TAG)
    }

    override fun onQuerySuccess(data: List<Note>) {
        view?.displayNotes(data)
    }

    override fun onQueryError() {
        view?.displayError("Erro ao carregar dados")
    }
}