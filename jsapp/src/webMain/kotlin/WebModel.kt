import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.kodein.di.direct
import org.kodein.di.instance
import ru.otus.arch.Model
import ru.otus.arch.data.AppGesture
import ru.otus.arch.data.AppUiState
import ru.otus.arch.di.di

@JsExport
class WebModel {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private val model: Model = di.direct.instance()

    init {
        model.uiState.onEach { stateObserver?.invoke(it) }.launchIn(scope)
    }

    private var stateObserver: ((AppUiState) -> Unit)? = null

    @JsName("setStateObserver")
    fun setStateObserver(observer: ((AppUiState) -> Unit)?) {
        stateObserver = observer
    }

    @JsName("process")
    fun process(gesture: AppGesture) {
        model.process(gesture)
    }

    @JsName("clear")
    fun clear() {
        scope.cancel()
    }
}