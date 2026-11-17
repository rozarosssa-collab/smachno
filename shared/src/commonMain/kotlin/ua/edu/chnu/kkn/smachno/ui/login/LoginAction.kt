package ua.edu.chnu.kkn.smachno.ui.login

internal sealed interface LoginAction {
    data class UsernameChanged(val value: String) : LoginAction
    data class PasswordChanged(val value: String) : LoginAction
    data object TogglePasswordVisibility : LoginAction
    data object FillDemoCredentials : LoginAction
    data object SignIn : LoginAction
}
