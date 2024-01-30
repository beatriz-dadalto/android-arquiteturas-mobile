package co.tiagoaguiar.evernotekt

import co.tiagoaguiar.evernotekt.model.Note
import org.junit.Assert
import org.junit.Test

class NoteTest {

    @Test
    fun `test should format date pattern to month and year`(){
        val note = Note(title = "NotaA", body = "NotaA conteúdo", date = "20/02/2024")

        Assert.assertEquals("Fev. 2024", note.createdDate)
    }

    @Test
    fun `test should format date case empty`(){
        val note = Note(title = "NotaA", body = "NotaA conteúdo", date = "")

        Assert.assertEquals("", note.createdDate)
    }

    @Test
    fun `test should format date case null`(){
        val note = Note(title = "NotaA", body = "NotaA conteúdo", date = null)

        Assert.assertEquals("", note.createdDate)
    }
}