import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel

actual abstract class ViewModel {
  actual val viewModelScope: CoroutineScope
    get() = CoroutineScope(SupervisorJob() + mainDispatcher)

  protected actual open fun onCleared() {
    viewModelScope.cancel()
  }
}