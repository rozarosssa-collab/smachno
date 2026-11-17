package ua.edu.chnu.kkn.smachno.ui.login

internal sealed interface LoginEvent {
    data class SignedIn(val username: String) : LoginEvent
}
