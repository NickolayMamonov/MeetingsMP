package dev.whysoezzy.meetings.compose.viewmodel

import androidx.lifecycle.ViewModel
import dev.whysoezzy.meetings.compose.ui.navigation.AuthCheckNavEvent
import dev.whysoezzy.meetings.domain.usecase.IsLoggedInUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class AuthCheckViewModel(
    private val isLoggedInUseCase: IsLoggedInUseCase,
) : ViewModel() {

    private val _navEvent = MutableSharedFlow<AuthCheckNavEvent>()
    val navEvent: SharedFlow<AuthCheckNavEvent> = _navEvent.asSharedFlow()

    suspend fun checkAuth() {
        val isLoggedIn = isLoggedInUseCase()
        _navEvent.emit(
            if (isLoggedIn) AuthCheckNavEvent.NavigateToMain
            else AuthCheckNavEvent.NavigateToAuth
        )
    }
}
