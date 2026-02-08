package afc.musicapp.uistate.vm

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import afc.musicapp.domain.logic.impure.iface.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.coroutines.CoroutineContext

open class BaseVm(protected val dispatchers: Dispatchers) : ViewModel() {
    private var loadingObjects: Int = 0
    private val loadMutex: Mutex = Mutex()
    var isLoading by mutableStateOf(false)

    protected suspend fun <T> loadIndicatorSuspend(block: suspend () -> T): T? {
        loadMutex.withLock {
            loadingObjects++
        }
        if (!isLoading) {
            isLoading = true
        }
        try {
            return block()
        } finally {
            loadMutex.withLock {
                loadingObjects--
                if (loadingObjects == 0) {
                    isLoading = false
                }
            }
        }
        return null
    }

    protected fun loadIndicator(context: CoroutineContext = dispatchers.default, block: suspend () -> Unit) {
        viewModelScope.launch(context) {
            loadIndicatorSuspend {
                block()
            }
        }
    }
}