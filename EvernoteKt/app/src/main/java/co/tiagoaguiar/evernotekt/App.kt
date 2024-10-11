package co.tiagoaguiar.evernotekt

import android.app.Application
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.Router

class App: Application() {

    companion object {
        lateinit var INSTANCE: App
    }

    lateinit var cicerone: Cicerone<Router>

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
        this.cicerone = Cicerone.create()
    }
}