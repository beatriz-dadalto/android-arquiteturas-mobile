package co.tiagoaguiar.evernotekt

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import co.tiagoaguiar.evernotekt.model.Note
import co.tiagoaguiar.evernotekt.model.RemoteDataSource
import com.google.android.material.navigation.NavigationView
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.app_bar_home.*
import kotlinx.android.synthetic.main.content_home.*
import retrofit2.Callback
import retrofit2.Response


class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val dataSource = RemoteDataSource()

    // compositeDisposable -> todos observers que podem ser descartados serão limpados então os os observables não emitirão mais msg
    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        setupViews()
    }

    private fun setupViews() {
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
            this,
            drawer_layout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        nav_view.setNavigationItemSelectedListener(this)

        val divider = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        home_recycler_view.addItemDecoration(divider)
        home_recycler_view.layoutManager = LinearLayoutManager(this)

        fab.setOnClickListener {
            val intent = Intent(baseContext, FormActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        getAllNotes()
    }

    override fun onStop() {
        super.onStop()
        // todos observers que podem ser descartados serão limpados então os os observables não emitirão mais msg
        compositeDisposable.clear()
    }

    // requisição no RETROFIT
    private fun getAllNotes() {
        val disposable = dataSource.listNotes()
            .subscribeOn(Schedulers.io()) // Thread Parallel
            .observeOn(AndroidSchedulers.mainThread()) // main Thread
            .subscribeWith(notesObserver)
        // retirar o observer do observable
        compositeDisposable.add(disposable)
    }

    private val notesObserver: DisposableObserver<List<Note>>
        get() = object : DisposableObserver<List<Note>>() {
            override fun onNext(notes: List<Note>) {
                displayNotes(notes)
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
                displayError("Erro ao carregar notas.")
            }

            override fun onComplete() {
                println("Completed!")
            }
        }

    fun displayError(message: String) {
        showToast(message)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun displayNotes(notes: List<Note>) {
        // progress
        if (notes.isNotEmpty()) {
            home_recycler_view.adapter = NoteAdapter(notes) { note ->
                val intent = Intent(baseContext, FormActivity::class.java)
                intent.putExtra("noteId", note.id)
                startActivity(intent)
            }
        } else {
            // no data
        }
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        return if (id == R.id.action_settings) {
            true
        } else super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == R.id.nav_all_notes) {
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

}