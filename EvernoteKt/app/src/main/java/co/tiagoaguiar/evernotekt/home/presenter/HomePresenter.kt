package co.tiagoaguiar.evernotekt.home.presenter

import co.tiagoaguiar.evernotekt.home.Home
import co.tiagoaguiar.evernotekt.model.Note
import co.tiagoaguiar.evernotekt.model.RemoteDataSource
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

/*
    controller, lógica fica aqui. devolve informações pra view e pede informações pro model
 */
class HomePresenter(
    private val view: Home.View,
    private val dataSource: RemoteDataSource
) : Home.Presenter {

    companion object {
        private const val TAG = "HomePresenter"
    }

    // compositeDisposable -> todos observers que podem ser descartados serão limpos então os os observables não emitirão mais msg
    private val compositeDisposable = CompositeDisposable()

    private val notesObserver: DisposableObserver<List<Note>>
        get() = object : DisposableObserver<List<Note>>() {
            override fun onNext(notes: List<Note>) {
                if (notes.isNotEmpty()) {
                    view.displayNotes(notes)
                } else {
                    view.displayEmptyNotes()
                }
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
                view.displayError("Erro ao carregar notas.")
            }

            override fun onComplete() {
                println("Completed!")
            }
        }

    override fun stop() {
        // compositeDisposable -> todos observers que podem ser descartados serão limpos então os os observables não emitirão mais msg
        compositeDisposable.clear()
    }

    private val notesObservable: Observable<List<Note>>
        get() = dataSource.listNotes()

    // requisição no RETROFIT
    override fun getAllNotes() {
        val disposable = notesObservable
            .subscribeOn(Schedulers.io()) // Thread Parallel
            .observeOn(AndroidSchedulers.mainThread()) // main Thread
            .subscribeWith(notesObserver)
        // retirar o observer do observable
        compositeDisposable.add(disposable)
    }
}