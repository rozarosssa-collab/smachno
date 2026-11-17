package ua.edu.chnu.kkn.smachno.domain.auth

/** Ім'я користувача, під яким виконано вхід. */
internal class SignedInUsernameUseCase(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(): String = authRepository.signedInUsername()
}
