package co.tiagoaguiar.evernotekt.addNote.presenter

import co.tiagoaguiar.evernotekt.addNote.AddNote
import co.tiagoaguiar.evernotekt.model.Note
import co.tiagoaguiar.evernotekt.model.RemoteDataSource
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

class AddNotePresenter(
    private val view: AddNote.View,
    private val dataSource: RemoteDataSource
) : AddNote.Presenter {

    // compositeDisposable -> todos observers que podem ser descartados serão limpos então os os observables não emitirão mais msg
    private val compositeDisposable = CompositeDisposable()

    private val createNoteObserver: DisposableObserver<Note>
        get() = object : DisposableObserver<Note>() {
            override fun onNext(note: Note) {
                view.returnToHome()
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
                view.displayError("Erro ao criar a nota.")
            }

            override fun onComplete() {
                println("Completed!")
            }
        }

    private val getNoteObserver: DisposableObserver<Note>
        get() = object : DisposableObserver<Note>() {
            override fun onNext(note: Note) {
                view.displayNote(note.title ?: "", note.body ?: "")
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
                view.displayError("Erro ao carregar notas.")
            }

            override fun onComplete() {
                println("Completed!")
            }

        }

    override fun createNote(title: String, body: String) {
        if (title.isEmpty() || body.isEmpty()) {
            view.displayError("Título e conteúdo da nota devem ser informados!")
            return
        }

        val note = Note(title = title, body = body)
        val disposable = dataSource.createNote(note)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(createNoteObserver)
        // retirar o observer do observable
        compositeDisposable.add(disposable)
    }

    override fun getNote(id: Int) {
        val disposable = dataSource.getNote(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(getNoteObserver)
        // retirar o observer do observable
        compositeDisposable.add(disposable)
    }

    override fun stop() {
        // compositeDisposable -> todos observers que podem ser descartados serão limpos então os os observables não emitirão mais msg
        compositeDisposable.clear()
    }
}