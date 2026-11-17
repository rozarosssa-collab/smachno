package ua.edu.chnu.kkn.smachno.domain.onboarding

import ua.edu.chnu.kkn.smachno.domain.PreferencesRepository

/** Позначає онбордінг як пройдений, щоб більше його не показувати. */
internal class MarkOnboardingPassedUseCase(
    private val preferencesRepository: PreferencesRepository,
) {
    operator fun invoke() = preferencesRepository.setOnboardingPassed(true)
}
