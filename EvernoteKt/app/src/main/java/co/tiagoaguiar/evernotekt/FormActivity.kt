package co.tiagoaguiar.evernotekt

import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import co.tiagoaguiar.evernotekt.model.Note
import co.tiagoaguiar.evernotekt.model.RemoteDataSource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_form.*
import kotlinx.android.synthetic.main.content_form.*
import retrofit2.Callback
import retrofit2.Response

class FormActivity : AppCompatActivity(), TextWatcher {

    private var toSave: Boolean = false
    private var noteId: Int? = null

    private val dataSource = RemoteDataSource()

    // compositeDisposable -> todos observers que podem ser descartados serão limpados então os os observables não emitirão mais msg
    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form)

        noteId = intent.extras?.getInt("noteId")

        setupViews()
    }

    override fun onStart() {
        super.onStart()
        noteId?.let {
            getNote(it)
        }
    }

    override fun onStop() {
        super.onStop()
        // compositeDisposable -> todos observers que podem ser descartados serão limpados então os os observables não emitirão mais msg
        compositeDisposable.clear()
    }

    private fun getNote(noteId: Int) {
        val disposable = dataSource.getNote(noteId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(getNoteObserver)
        // retirar o observer do observable
        compositeDisposable.add(disposable)
    }

    private fun createNote(note: Note) {
        val disposable = dataSource.createNote(note)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(createNoteObserver)
        // retirar o observer do observable
        compositeDisposable.add(disposable)
    }

    private val getNoteObserver: DisposableObserver<Note>
        get() = object : DisposableObserver<Note>() {
            override fun onNext(note: Note) {
                displayNote(note)
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
                displayError("Erro ao carregar notas.")
            }

            override fun onComplete() {
                println("Completed!")
            }

        }

    private val createNoteObserver: DisposableObserver<Note>
        get() = object : DisposableObserver<Note>() {
            override fun onNext(note: Note) {
                finish()
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
                displayError("Erro ao carregar notas")
            }

            override fun onComplete() {
                println("Completed!")
            }
        }

    private fun setupViews() {
        setSupportActionBar(toolbar)
        toggleToolbar(R.drawable.ic_arrow_back_black_24dp)

        note_title.addTextChangedListener(this)
        note_editor.addTextChangedListener(this)
    }

    private fun toggleToolbar(@DrawableRes icon: Int) {
        supportActionBar?.let {
            it.title = null
            val upArrow = ContextCompat.getDrawable(this, icon)
            val colorFilter =
                PorterDuffColorFilter(
                    ContextCompat.getColor(this, R.color.colorAccent),
                    PorterDuff.Mode.SRC_ATOP
                )
            upArrow?.colorFilter = colorFilter
            it.setHomeAsUpIndicator(upArrow)
            it.setDisplayHomeAsUpEnabled(true)
        }
    }

    fun displayError(message: String) {
        showToast(message)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun displayNote(note: Note?) {
        // progress
        if (note != null) {
            note_title.setText(note.title)
            note_editor.setText(note.body)
        } else {
            // no data
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            return if (toSave && noteId == null) {
                val note = Note()
                note.title = note_title.text.toString()
                note.body = note_editor.text.toString()

                createNote(note)

                true
            } else {
                finish()
                true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
    }

    override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
        toSave =
            if (note_editor.text.toString().isEmpty() && note_title.text.toString().isEmpty()) {
                toggleToolbar(R.drawable.ic_arrow_back_black_24dp)
                false
            } else {
                toggleToolbar(R.drawable.ic_done_black_24dp)
                true
            }
    }

    override fun afterTextChanged(editable: Editable) {
    }

}