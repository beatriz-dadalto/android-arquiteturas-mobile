package co.tiagoaguiar.evernotekt

import co.tiagoaguiar.evernotekt.home.Home
import co.tiagoaguiar.evernotekt.home.presenter.HomePresenter
import co.tiagoaguiar.evernotekt.model.Note
import co.tiagoaguiar.evernotekt.model.RemoteDataSource
import io.reactivex.Observable
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class HomePresenterTests {

    @Rule
    @JvmField
    var testSchedulerRule = RxSchedulerRule()

    @Mock
    private lateinit var mockView: Home.View

    @Mock
    private lateinit var mockDataSource: RemoteDataSource

    lateinit var homePresenter: HomePresenter

    private val fakeAllNotes: List<Note>
        get() = arrayListOf(
            Note(1, "NotaA", "NotaA descrição", "01/10/2024", "Seja bem vindo a nota A"),
            Note(2, "NotaB", "NotaB descrição", "02/10/2024", "Seja bem vindo a nota B"),
            Note(3, "NotaC", "NotaC descrição", "03/10/2024", "Seja bem vindo a nota C")
        )

    @Before
    fun setup() {
        homePresenter = HomePresenter(mockView, mockDataSource)
    }

    @Test
    fun `test must get all notes`() {
        // GIVEN
        Mockito.doReturn(Observable.just(fakeAllNotes)).`when`(mockDataSource).listNotes()
        // WHEN
        homePresenter.getAllNotes()
        // THEN
        Mockito.verify(mockDataSource).listNotes()
        Mockito.verify(mockView).displayNotes(fakeAllNotes)
    }

    @Test
    fun `test must show empty notes`() {
        // GIVEN
        Mockito.doReturn(Observable.just(arrayListOf<Note>())).`when`(mockDataSource).listNotes()
        // WHEN
        homePresenter.getAllNotes()
        // THEN
        Mockito.verify(mockDataSource).listNotes()
        Mockito.verify(mockView).displayEmptyNotes()
    }
}