package co.tiagoaguiar.evernotekt

import co.tiagoaguiar.evernotekt.addNote.AddNote
import co.tiagoaguiar.evernotekt.addNote.presenter.AddNotePresenter
import co.tiagoaguiar.evernotekt.home.Home
import co.tiagoaguiar.evernotekt.home.presenter.HomePresenter
import co.tiagoaguiar.evernotekt.model.Note
import co.tiagoaguiar.evernotekt.model.RemoteDataSource
import com.nhaarman.mockito_kotlin.anyOrNull
import io.reactivex.Observable
import org.hamcrest.CoreMatchers
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class AddNotePresenterTests : BaseTest() {

    @Rule
    @JvmField
    var testSchedulerRule = RxSchedulerRule()

    @Mock
    private lateinit var mockView: AddNote.View

    @Mock
    private lateinit var mockDataSource: RemoteDataSource

    @Captor
    private lateinit var noteArgCaptor: ArgumentCaptor<Note>

    lateinit var addNotePresenter: AddNotePresenter

    @Before
    fun setup() {
        addNotePresenter = AddNotePresenter(mockView, mockDataSource)
    }

    @Test
    fun `test must not add note with empty body`() {
        // WHEN
        addNotePresenter.createNote("", "")
        // THEN
        Mockito.verify(mockView).displayError("Título e conteúdo da nota devem ser informados!")
    }

    @Test
    fun `test must add note`() {
        // GIVEN
        val note = Note(title = "NotaA", body = "Corpo NotaA")
        Mockito.doReturn(Observable.just(note)).`when`(mockDataSource)
            .createNote(captureArg(noteArgCaptor))
        // WHEN
        addNotePresenter.createNote("NotaA", body = "Corpo NotaA")
        // THEN
        Mockito.verify(mockDataSource).createNote(captureArg(noteArgCaptor))

        Assert.assertThat(noteArgCaptor.value.title, CoreMatchers.equalTo("NotaA"))
        Assert.assertThat(noteArgCaptor.value.body, CoreMatchers.equalTo("Corpo NotaA"))

        Mockito.verify(mockView).returnToHome()
    }

    @Test
    fun `test must show error message when creation failure`() {
        // GIVEN
        Mockito.doReturn(Observable.error<Throwable>(Throwable("server error")))
            .`when`(mockDataSource).createNote(
                anyOrNull()
            )
        // WHEN
        addNotePresenter.createNote("NotaA", "Corpo NotaA")
        // THEN
        Mockito.verify(mockDataSource).createNote(captureArg(noteArgCaptor))

        Assert.assertThat(noteArgCaptor.value.title, CoreMatchers.equalTo("NotaA"))
        Assert.assertThat(noteArgCaptor.value.body, CoreMatchers.equalTo("Corpo NotaA"))

        Mockito.verify(mockView).displayError("Erro ao criar a nota.")
    }
}