package co.tiagoaguiar.evernotekt

import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

class RxSchedulerRule : TestRule {
    override fun apply(base: Statement?, description: Description?): Statement {
        return object : Statement() {
            override fun evaluate() {
                // toda vez que precisar chamar o Scheduler.Io vai sobreescrever para o trampoline que vai rodar em FILA e nao em threads
                RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
                RxJavaPlugins.setComputationSchedulerHandler { Schedulers.trampoline() }
                RxJavaPlugins.setNewThreadSchedulerHandler { Schedulers.trampoline() }

                RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }

                try {
                    base?.evaluate()
                } finally {
                    RxAndroidPlugins.reset()
                    RxJavaPlugins.reset()
                }
            }

        }
    }
}