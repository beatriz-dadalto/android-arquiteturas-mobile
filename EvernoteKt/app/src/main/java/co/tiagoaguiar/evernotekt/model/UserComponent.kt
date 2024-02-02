package co.tiagoaguiar.evernotekt.model

import dagger.Component

/*
    interface responsável por prover uma instancia do tipo User
 */

@Component
interface UserComponent {

    fun getUser(): User
}