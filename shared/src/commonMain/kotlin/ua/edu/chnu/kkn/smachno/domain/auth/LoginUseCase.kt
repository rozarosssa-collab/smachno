package ua.edu.chnu.kkn.smachno.domain.auth

import ua.edu.chnu.kkn.smachno.data.common.Result

/** Авторизація користувача. Токени репозиторій зберігає сам. */
internal class LoginUseCase(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(username: String, password: String): Result<Unit> =
        authRepository.login(username.trim(), password)
}
