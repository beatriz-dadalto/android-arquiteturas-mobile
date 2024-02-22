package co.tiagoaguiar.evernotekt

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MediatorLiveData
import co.tiagoaguiar.evernotekt.data.INoteRepository
import co.tiagoaguiar.evernotekt.data.model.Note
import co.tiagoaguiar.evernotekt.viewmodel.AddViewModel
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.verify
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class AddViewModelTest {

    /*
        livedata e viewmodel como testes tambem na arquitetura MVVM.
        -> faz os testes baseados no viewModel
        -> faz com que os repositories sejam mockados para validar as chamadas deles vindas dos
        viewModels assim conseguimos validar se o dado que estah retornando eh o
        mesmo do banco
        -> e tambem conseguimos validar formularios.
    */

    // fazer com que as regras do LiveData funcione
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private lateinit var viewModel: AddViewModel

    @Mock
    lateinit var repository: INoteRepository

    @Before
    fun setup() {
        viewModel = AddViewModel(repository)
    }

    @Test
    fun `test must not save note without title`() {
        viewModel.title.set("")
        viewModel.body.set("")

        viewModel.createNote()

        Assert.assertEquals(false, viewModel.saved.value)
    }

    @Test
    fun `test must save note`() {
        viewModel.title.set("NotaA")
        viewModel.body.set("BodyA")

        viewModel.createNote()

        Assert.assertEquals(true, viewModel.saved.value)

        val note = Note(title = "NotaA", body = "BodyA")
        verify(repository).createNote(note)
    }

    @Test
    fun `test must get a note`() {
        val noteA = Note(id = 1, title = "NotaA", body = "BodyA")

        val liveData = MediatorLiveData<Note>()
        liveData.value = noteA

        doReturn(liveData).`when`(repository).getNote(1)

        viewModel.getNote(1)

        // valide que a nota que veio do repository eh a mesma nota fornecida pelo LiveData
        Assert.assertEquals(noteA, viewModel.getNote(1).value)
    }
}