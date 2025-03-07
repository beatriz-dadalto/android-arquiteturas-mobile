package co.tiagoaguiar.evernotekt.view.activities

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
import co.tiagoaguiar.evernotekt.R
import co.tiagoaguiar.evernotekt.data.NoteInteractorImpl
import co.tiagoaguiar.evernotekt.data.model.Note
import co.tiagoaguiar.evernotekt.data.model.NoteState
import co.tiagoaguiar.evernotekt.presenter.FormPresenter
import co.tiagoaguiar.evernotekt.view.IFormView
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_form.*
import kotlinx.android.synthetic.main.content_form.*

class FormActivity : AppCompatActivity(), IFormView, TextWatcher {

    private var toSave: Boolean = false
    private var noteId: Int? = null

    private lateinit var presenter: FormPresenter

    private val publish: PublishSubject<Note> = PublishSubject.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form)

        noteId = intent.extras?.getInt("noteId")

        presenter = FormPresenter(NoteInteractorImpl())
        presenter.bind(this, noteId)

        setupViews()
    }

    override fun render(state: NoteState) {
        when (state) {
            is NoteState.LoadingState -> renderLoadingState()
            is NoteState.SingleDataState -> renderDataState(state)
            is NoteState.ErrorState -> renderErrorState(state)
            is NoteState.FinishState -> renderFinishState()
        }
    }

    private fun renderFinishState() {
        finish()
    }

    private fun renderErrorState(state: NoteState.ErrorState) {
        showToast(state.data)
    }

    private fun renderDataState(state: NoteState.SingleDataState) {
        val note = state.data
        note_title.setText(note.title)
        note_editor.setText(note.body)
    }

    private fun renderLoadingState() {
        showToast("start loading")
    }

    override fun displayNoteIntent(): Observable<Unit> = Observable.just(Unit)

    override fun addNoteIntent(): Observable<Note> = publish

    private fun saveNoteClicked() {
        if (note_title.text!!.isEmpty() || note_editor.text!!.isEmpty()) {
            showToast("Título e nota deve ser informado")
            return
        }
        publish.onNext(Note(title = note_title.text.toString(), body = note_editor.text.toString()))
    }

    private fun backClicked() {
        finish()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            return if (toSave && noteId == null) {
                saveNoteClicked()
                true
            } else {
                backClicked()
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

    private fun setupViews() {
        setSupportActionBar(toolbar)
        toggleToolbar(R.drawable.ic_arrow_back_black_24dp)

        note_title.addTextChangedListener(this)
        note_editor.addTextChangedListener(this)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun onStop() {
        super.onStop()
        presenter.unbind()
    }

}