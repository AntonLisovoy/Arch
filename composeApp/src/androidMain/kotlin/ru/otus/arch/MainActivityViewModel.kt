package ru.otus.arch

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.instance
import ru.otus.arch.di.appModule

class MainActivityViewModel(application: Application) : AndroidViewModel(application), DIAware {
    override val di: DI = DI {
        extend((application as DIAware).di)
        import(appModule)
    }

    val model: Model by di.instance()
}