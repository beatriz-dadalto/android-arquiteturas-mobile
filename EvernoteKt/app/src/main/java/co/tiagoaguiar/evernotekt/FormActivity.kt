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
import kotlinx.android.synthetic.main.activity_form.*
import kotlinx.android.synthetic.main.content_form.*
import retrofit2.Callback
import retrofit2.Response

/**
 *
 * Setembro, 24 2019
 * @author suporte@moonjava.com.br (Tiago Aguiar).
 */
class FormActivity : AppCompatActivity(), TextWatcher {

    private var toSave: Boolean = false
    private var noteId: Int? = null

    private val dataSource = RemoteDataSource()

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

    private fun getNote(noteId: Int) {
        dataSource.getNote(noteId, callback)
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


    private val callback: Callback<Note>
        get() = object : Callback<Note> {

            override fun onFailure(call: retrofit2.Call<Note>, t: Throwable) {
                t.printStackTrace()
                displayError("Erro ao carregar nota")
            }

            override fun onResponse(
                call: retrofit2.Call<Note>,
                response: Response<Note>
            ) {
                if (response.isSuccessful) {
                    val note = response.body()
                    displayNote(note)
                }
            }

        }

    private val callbackCreate: Callback<Note>
        get() = object : Callback<Note> {

            override fun onFailure(call: retrofit2.Call<Note>, t: Throwable) {
                t.printStackTrace()
                displayError("Erro ao criar nota")
            }

            override fun onResponse(
                call: retrofit2.Call<Note>,
                response: Response<Note>
            ) {
                if (response.isSuccessful) {
                    finish()
                }
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

                dataSource.createNote(note, callbackCreate)

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