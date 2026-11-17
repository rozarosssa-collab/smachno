package ua.edu.chnu.kkn.smachno.domain.onboarding

import ua.edu.chnu.kkn.smachno.domain.PreferencesRepository

/** Чи переглядав користувач онбордінг. */
internal class IsOnboardingPassedUseCase(
    private val preferencesRepository: PreferencesRepository,
) {
    operator fun invoke(): Boolean = preferencesRepository.isOnboardingPassed()
}
