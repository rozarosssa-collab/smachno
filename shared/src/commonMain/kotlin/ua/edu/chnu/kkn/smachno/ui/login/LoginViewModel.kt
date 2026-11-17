package ua.edu.chnu.kkn.smachno.ui.login

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
import smachno.shared.generated.resources.Res
import smachno.shared.generated.resources.login_error_password_short
import smachno.shared.generated.resources.login_error_required
import smachno.shared.generated.resources.login_error_username_short
import ua.edu.chnu.kkn.smachno.core.logging.AppLogger
import ua.edu.chnu.kkn.smachno.data.common.onFailure
import ua.edu.chnu.kkn.smachno.data.common.onSuccess
import ua.edu.chnu.kkn.smachno.domain.auth.LoginUseCase

private const val MIN_USERNAME_LENGTH = 3
private const val MIN_PASSWORD_LENGTH = 6

// dummyjson не дозволяє реєструвати своїх користувачів, тому для перевірки
// застосунку є готовий тестовий акаунт.
private const val DEMO_USERNAME = "emilys"
private const val DEMO_PASSWORD = "emilyspass"

@Stable
internal class LoginViewModel(
    private val loginUseCase: LoginUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state.asStateFlow()

    private val _events = Channel<LoginEvent>(capacity = Channel.BUFFERED)
    val events: Flow<LoginEvent> = _events.receiveAsFlow()

    fun onAction(action: LoginAction) {
        when (action) {
            is LoginAction.UsernameChanged ->
                _state.update { it.copy(username = action.value, usernameError = null) }

            is LoginAction.PasswordChanged ->
                _state.update { it.copy(password = action.value, passwordError = null) }

            LoginAction.TogglePasswordVisibility ->
                _state.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }

            LoginAction.FillDemoCredentials -> _state.update {
                it.copy(
                    username = DEMO_USERNAME,
                    password = DEMO_PASSWORD,
                    usernameError = null,
                    passwordError = null,
                )
            }

            LoginAction.SignIn -> signIn()
        }
    }

    private fun signIn() {
        if (_state.value.isLoading) return
        if (!validate()) return

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val current = _state.value
            loginUseCase(current.username, current.password)
                .onSuccess {
                    _events.trySend(LoginEvent.SignedIn)
                }
                .onFailure { message ->
                    AppLogger.e { "Не вдалось увійти: $message" }
                    _events.trySend(LoginEvent.ShowError(message))
                }
            _state.update { it.copy(isLoading = false) }
        }
    }

    /** Перевірка полів до того, як іти в мережу. */
    private fun validate(): Boolean {
        val current = _state.value
        val username = current.username.trim()

        val usernameError = when {
            username.isEmpty() -> Res.string.login_error_required
            username.length < MIN_USERNAME_LENGTH -> Res.string.login_error_username_short
            else -> null
        }
        val passwordError = when {
            current.password.isEmpty() -> Res.string.login_error_required
            current.password.length < MIN_PASSWORD_LENGTH -> Res.string.login_error_password_short
            else -> null
        }

        _state.update { it.copy(usernameError = usernameError, passwordError = passwordError) }
        return usernameError == null && passwordError == null
    }
}
