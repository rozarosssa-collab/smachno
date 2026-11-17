package ua.edu.chnu.kkn.smachno.ui.login

import androidx.compose.runtime.Immutable
import org.jetbrains.compose.resources.StringResource

/** Повний стан екрана входу. Екран малює тільки те, що тут лежить. */
@Immutable
internal data class LoginState(
    val username: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val usernameError: StringResource? = null,
    val passwordError: StringResource? = null,
) {
    val isSubmitEnabled: Boolean
        get() = username.isNotBlank() && password.isNotBlank()
}
