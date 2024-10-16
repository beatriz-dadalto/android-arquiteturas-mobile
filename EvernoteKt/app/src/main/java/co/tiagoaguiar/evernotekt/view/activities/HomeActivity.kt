package co.tiagoaguiar.evernotekt.view.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import co.tiagoaguiar.evernotekt.App
import co.tiagoaguiar.evernotekt.IHome
import co.tiagoaguiar.evernotekt.R
import co.tiagoaguiar.evernotekt.data.model.Note
import co.tiagoaguiar.evernotekt.interactor.HomeInteractor
import co.tiagoaguiar.evernotekt.presenter.HomePresenter
import co.tiagoaguiar.evernotekt.view.adapters.NoteAdapter
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.app_bar_home.*
import kotlinx.android.synthetic.main.content_home.*
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.commands.Forward

class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    IHome.View {

    private val navigator: Navigator? by lazy {
        Navigator { command ->
            if (command is Forward) {
                when (command.screenKey) {
                    FormActivity.TAG -> startActivity(
                        Intent(
                            this@HomeActivity,
                            FormActivity::class.java
                        )
                    )
                }
            }
        }
    }

    private lateinit var presenter: IHome.Presenter

    private val router: Router? by lazy {
        App.INSTANCE.cicerone.router
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        presenter = HomePresenter(this, HomeInteractor(), router)

        setupViews()
    }

    override fun onResume() {
        super.onResume()
        App.INSTANCE.cicerone.navigatorHolder.setNavigator(navigator)
        presenter.onStart()
    }

    override fun onPause() {
        super.onPause()
        App.INSTANCE.cicerone.navigatorHolder.removeNavigator()
    }

    override fun onStart() {
        super.onStart()
        presenter.onStart()
    }

    override fun displayError(message: String) {
        showToast(message)
    }

    override fun displayNotes(notes: List<Note>) {
        // progress
        if (notes.isNotEmpty()) {
            home_recycler_view.adapter =
                NoteAdapter(notes) { note ->
                    val intent = Intent(
                        baseContext,
                        FormActivity::class.java
                    )
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

    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    fun goToAddActivity(view: View) {
        presenter.addNoteClick()
    }

}