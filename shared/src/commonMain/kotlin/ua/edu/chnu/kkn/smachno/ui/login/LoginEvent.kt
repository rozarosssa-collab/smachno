package ua.edu.chnu.kkn.smachno.ui.login

internal sealed interface LoginEvent {
    data object SignedIn : LoginEvent
    data class ShowError(val message: String) : LoginEvent
}
