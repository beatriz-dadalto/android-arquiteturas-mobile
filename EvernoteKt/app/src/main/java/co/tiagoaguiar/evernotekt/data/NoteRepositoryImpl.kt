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
                override fun onNext(note: List<Note>) {
                    data.postValue(note)
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
        val data = MutableLiveData<Note?>()

        val disposable = remoteDataSource.getNote(noteId)
            .observeOn(Schedulers.io())
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableObserver<Note>() {
                override fun onComplete() {
                    println("complete")
                }

                override fun onNext(note: Note) {
                    data.postValue(note)
                }

                override fun onError(e: Throwable) {
                    e.printStackTrace()
                    data.postValue(null)
                }
            })

        compositeDisposable.add(disposable)

        return data
    }

    override fun createNote(note: Note): LiveData<Note?> {
        val data = MutableLiveData<Note?>()

        val disposable = remoteDataSource.createNote(note)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableObserver<Note>() {

                override fun onComplete() {
                    println("complete")
                }

                override fun onNext(note: Note) {
                    data.postValue(note)
                }

                override fun onError(e: Throwable) {
                    e.printStackTrace()
                    data.postValue(null)
                }
            })

        compositeDisposable.add(disposable)
        return data
    }
}