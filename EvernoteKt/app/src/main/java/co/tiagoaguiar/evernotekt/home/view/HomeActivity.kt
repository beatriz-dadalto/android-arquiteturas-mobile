package co.tiagoaguiar.evernotekt.home.view

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
import co.tiagoaguiar.evernotekt.addNote.view.FormActivity
import co.tiagoaguiar.evernotekt.R
import co.tiagoaguiar.evernotekt.home.Home
import co.tiagoaguiar.evernotekt.home.presenter.HomePresenter
import co.tiagoaguiar.evernotekt.model.Note
import co.tiagoaguiar.evernotekt.model.RemoteDataSource
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.app_bar_home.*
import kotlinx.android.synthetic.main.content_home.*


class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    Home.View {

    private lateinit var homePresenter: HomePresenter
    private val dataSource = RemoteDataSource()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        setupPresenter()
        setupViews()
    }

    private fun setupPresenter() {
        // criar as dependencias
        val dataSource = RemoteDataSource()
        homePresenter = HomePresenter(this, dataSource)
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
        homePresenter.getAllNotes()
    }

    override fun onStop() {
        super.onStop()
        homePresenter.stop()
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

    override fun displayError(message: String) {
        showToast(message)
    }

    override fun displayEmptyNotes() {
        // TODO("Not yet implemented")
    }

    override fun displayNotes(notes: List<Note>) {
        home_recycler_view.adapter = NoteAdapter(notes) { note ->
            val intent = Intent(baseContext, FormActivity::class.java)
            intent.putExtra("noteId", note.id)
            startActivity(intent)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

}