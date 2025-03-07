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
class FormPresenterTest {
    private lateinit var presenter: FormPresenter

    @Mock
    private lateinit var view: IForm.View

    @Mock
    private lateinit var interactor: IForm.Interactor

    @Before
    fun setup() {
        val cicerone = Cicerone.create()
        presenter = FormPresenter(view, interactor, cicerone.router)
    }

    @Test
    fun `add note with title and body`() {
        presenter.saveNote("TitleA", "DescA")

        verify(interactor).createNote(Note(title = "TitleA", body = "DescA"))
    }

    @Test
    fun `get note clicked`() {
        presenter.onStart(1)

        verify(interactor).getNote(1)
    }

    @Test
    fun `do not save note without body`() {
        presenter.saveNote("TitleA", body = "")

        verify(view).displayError("Título e nota deve ser informado")
    }
}