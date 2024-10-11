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
import co.tiagoaguiar.evernotekt.App
import co.tiagoaguiar.evernotekt.IForm
import co.tiagoaguiar.evernotekt.R
import co.tiagoaguiar.evernotekt.data.model.Note
import co.tiagoaguiar.evernotekt.interactor.FormInteractor
import co.tiagoaguiar.evernotekt.presenter.FormPresenter
import kotlinx.android.synthetic.main.activity_form.*
import kotlinx.android.synthetic.main.content_form.*
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.commands.Back

class FormActivity : AppCompatActivity(), IForm.View, TextWatcher {

    companion object {
        val TAG = "FormActivity"
    }

    private val navigator: Navigator? by lazy {
        Navigator { command ->
            if (command is Back) {
                finish()
            }
        }
    }

    private var toSave: Boolean = false
    private var noteId: Int? = null

    private lateinit var presenter: IForm.Presenter

    private val router: Router? by lazy {
        App.INSTANCE.cicerone.router
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form)

        noteId = intent.extras?.getInt("noteId")

        presenter = FormPresenter(this, FormInteractor(), router)

        setupViews()
    }

    override fun onStart() {
        super.onStart()
        presenter.onStart(noteId)
    }

    override fun onResume() {
        super.onResume()
        App.INSTANCE.cicerone.navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        super.onPause()
        App.INSTANCE.cicerone.navigatorHolder.removeNavigator()
    }

    private fun saveNoteClicked() {
        presenter.saveNote(note_title.text.toString(), note_editor.text.toString())
    }

    private fun backClicked() {
        presenter.backPressClick()
    }

    override fun displayNote(note: Note) {
        note_title.setText(note.title)
        note_editor.setText(note.body)
    }

    override fun displayError(message: String) {
        showToast(message)
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

}