package co.tiagoaguiar.evernotekt.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import co.tiagoaguiar.evernotekt.data.model.Note
import co.tiagoaguiar.evernotekt.data.model.RemoteDataSource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

class NoteRepositoryImpl : INoteRepository {

    private val remoteDataSource = RemoteDataSource()
    private val compositeDisposable = CompositeDisposable()

    override fun getAllNotes(): LiveData<List<Note>?> {
        /*
         todos que estiverem observando esse MutableLiveData, ou seja, os observadores, serão notificados.
         então nesse caso a Activity vai ser a observadora. Portanto ela vai conseguir escutar
         quando houver mudança de estado e evento e pra isso precisa criar uma classe
         viewModel pra observar esse MutableLiveData.
         -> viewModel vai intermediar a Activity e o repository.
         */
        val data = MutableLiveData<List<Note>?>()

        val disposable = remoteDataSource.listNotes()
            .subscribeOn(Schedulers.io()) // Thread Parallel
            .observeOn(AndroidSchedulers.mainThread()) // main Thread
            .subscribeWith(object : DisposableObserver<List<Note>>() {
                override fun onNext(notes: List<Note>) {
                    data.postValue(notes)
                }

                override fun onError(e: Throwable) {
                    e.printStackTrace()
                    data.postValue(null)
                }

                override fun onComplete() {
                    println("Completed!")
                }

            })

        // retirar o observer do observable
        compositeDisposable.add(disposable)
        return data
    }

    override fun getNote(noteId: Int): LiveData<Note?> {
        TODO()
    }

    override fun createNote(note: Note): LiveData<Note> {
        TODO()
    }
}