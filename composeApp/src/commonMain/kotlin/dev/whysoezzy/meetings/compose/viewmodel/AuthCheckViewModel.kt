package dev.whysoezzy.meetings.compose.viewmodel

import androidx.lifecycle.ViewModel
import dev.whysoezzy.meetings.compose.ui.navigation.AuthCheckNavEvent
import dev.whysoezzy.meetings.compose.ui.navigation.AuthCheckUiState
import dev.whysoezzy.meetings.domain.usecase.IsLoggedInUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthCheckViewModel(
    private val isLoggedInUseCase: IsLoggedInUseCase,
) : ViewModel() {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    private val _uiState = MutableStateFlow(AuthCheckUiState())
    val uiState: StateFlow<AuthCheckUiState> = _uiState.asStateFlow()

    private val _navEvent = MutableSharedFlow<AuthCheckNavEvent>()
    val navEvent: SharedFlow<AuthCheckNavEvent> = _navEvent.asSharedFlow()

    fun checkAuth() {
        scope.launch {
            val isLoggedIn = isLoggedInUseCase()
            _uiState.update {
                it.copy(isLoading = false, isLoggedIn = isLoggedIn)
            }
            if (isLoggedIn) {
                _navEvent.emit(AuthCheckNavEvent.NavigateToMain)
            } else {
                _navEvent.emit(AuthCheckNavEvent.NavigateToAuth)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        scope.cancel()
    }
}
