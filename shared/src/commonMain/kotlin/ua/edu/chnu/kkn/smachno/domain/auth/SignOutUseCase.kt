package ua.edu.chnu.kkn.smachno.domain.auth

/** Стирає токени, після чого застосунок знову просить увійти. */
internal class SignOutUseCase(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke() = authRepository.signOut()
}
