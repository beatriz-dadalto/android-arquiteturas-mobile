package co.tiagoaguiar.evernotekt.model

import javax.inject.Inject

/*
    -> Injete esses tipos de dados (Note) para o obj User.
    -> para o Dagger fazer isso precisa especificar para o Dagger qual tipo de instancia usará no futuro.
    -> Precisa criar um componente que vai gerenciar essas instancias. vamos criar uma interface
        UserComponent responsável por prover uma instancia do tipo User.
 */
class User @Inject constructor(private val note: Note) {

    fun showNoteTitle() {
        println("título: ${note.title}")
    }

}