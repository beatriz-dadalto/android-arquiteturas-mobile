package co.tiagoaguiar.evernotekt.presenter

import co.tiagoaguiar.evernotekt.data.IInteractor
import co.tiagoaguiar.evernotekt.data.model.NoteState
import co.tiagoaguiar.evernotekt.view.IHomeView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class HomePresenter(private var interactor: IInteractor) {

    private val compositeDisposable = CompositeDisposable()
    private lateinit var view: IHomeView

    fun bind(view: IHomeView) {
        this.view = view
        compositeDisposable.add(observeNoteDisplay())
    }

    fun unbind() {
        if (!compositeDisposable.isDisposed) {
            compositeDisposable.dispose()
        }
    }

    private fun observeNoteDisplay() = view.displayNotesIntent()
        .flatMap { interactor.getAllNotes() }
        .startWith(NoteState.LoadingState)
        .subscribeOn((Schedulers.io()))
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe { view.render(it) }

}