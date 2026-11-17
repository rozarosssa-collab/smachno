package ua.edu.chnu.kkn.smachno.domain.auth

/** Чи є збережений токен доступу. */
internal class IsSignedInUseCase(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(): Boolean = authRepository.isSignedIn()
}
