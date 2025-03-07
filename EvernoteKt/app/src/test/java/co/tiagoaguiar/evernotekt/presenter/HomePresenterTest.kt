package co.tiagoaguiar.evernotekt.presenter

import co.tiagoaguiar.evernotekt.data.model.Note
import com.nhaarman.mockito_kotlin.verify
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import ru.terrakok.cicerone.Cicerone

@RunWith(MockitoJUnitRunner::class)
class HomePresenterTest {
    private lateinit var presenter: HomePresenter

    @Mock
    private lateinit var view: IHome.View

    @Mock
    private lateinit var interactor: IHome.Interactor

    @Before
    fun setup() {
        val cicerone = Cicerone.create()
        presenter = HomePresenter(view, interactor, cicerone.router)
    }

    @Test
    fun `display notes when query success`() {
        val list = listOf(
            Note(id = 1, title = "TitleA", body = "DescA"),
            Note(id = 2, title = "TitleB", body = "DescB"),
        )

        presenter.onQuerySuccess(list)

        verify(view).displayNotes(list)
    }

    @Test
    fun `load notes`() {
        presenter.onStart()

        verify(interactor).loadNoteList()
    }

}