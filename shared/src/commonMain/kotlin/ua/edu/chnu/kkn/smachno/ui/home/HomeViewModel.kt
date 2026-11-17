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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ua.edu.chnu.kkn.smachno.core.logging.AppLogger
import ua.edu.chnu.kkn.smachno.data.common.onFailure
import ua.edu.chnu.kkn.smachno.data.common.onSuccess
import ua.edu.chnu.kkn.smachno.domain.auth.SignOutUseCase
import ua.edu.chnu.kkn.smachno.domain.auth.SignedInUsernameUseCase
import ua.edu.chnu.kkn.smachno.domain.recipes.ObtainRecipesUseCase

@Stable
internal class HomeViewModel(
    private val obtainRecipesUseCase: ObtainRecipesUseCase,
    private val signedInUsernameUseCase: SignedInUsernameUseCase,
    private val signOutUseCase: SignOutUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    private val _events = Channel<HomeEvent>(capacity = Channel.BUFFERED)
    val events: Flow<HomeEvent> = _events.receiveAsFlow()

    init {
        viewModelScope.launch {
            _state.update { it.copy(username = signedInUsernameUseCase()) }
        }
        loadRecipes()
    }

    fun onAction(action: HomeAction) {
        when (action) {
            HomeAction.Reload -> loadRecipes()
            HomeAction.SignOut -> viewModelScope.launch {
                signOutUseCase()
                _events.trySend(HomeEvent.SignedOut)
            }
        }
    }

    private fun loadRecipes() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }
            obtainRecipesUseCase()
                .onSuccess { recipes ->
                    _state.update { it.copy(isLoading = false, recipes = recipes) }
                }
                .onFailure { message ->
                    AppLogger.e { "Не вдалось завантажити рецепти: $message" }
                    _state.update { it.copy(isLoading = false, errorMessage = message) }
                }
        }
    }
}
