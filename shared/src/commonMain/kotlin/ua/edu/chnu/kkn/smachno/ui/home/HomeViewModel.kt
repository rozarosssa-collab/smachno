package ua.edu.chnu.kkn.smachno.ui.home

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import ua.edu.chnu.kkn.smachno.domain.auth.SignOutUseCase
import ua.edu.chnu.kkn.smachno.domain.auth.SignedInUsernameUseCase

@Stable
internal class HomeViewModel(
    private val signedInUsernameUseCase: SignedInUsernameUseCase,
    private val signOutUseCase: SignOutUseCase,
) : ViewModel() {

    private val _username = MutableStateFlow("")
    val username: StateFlow<String> = _username.asStateFlow()

    private val _events = Channel<HomeEvent>(capacity = Channel.BUFFERED)
    val events: Flow<HomeEvent> = _events.receiveAsFlow()

    init {
        viewModelScope.launch {
            _username.value = signedInUsernameUseCase()
        }
    }

    fun onAction(action: HomeAction) {
        when (action) {
            HomeAction.SignOut -> viewModelScope.launch {
                signOutUseCase()
                _events.trySend(HomeEvent.SignedOut)
            }
        }
    }
}

internal sealed interface HomeAction {
    data object SignOut : HomeAction
}

internal sealed interface HomeEvent {
    data object SignedOut : HomeEvent
}
