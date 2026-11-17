package ua.edu.chnu.kkn.smachno

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.navigation3.runtime.NavKey
import ua.edu.chnu.kkn.smachno.domain.onboarding.IsOnboardingPassedUseCase

/**
 * Вирішує, з якого екрана починати: якщо онбордінг уже переглядали,
 * одразу показуємо вхід.
 */
@Stable
internal class AppViewModel(
    isOnboardingPassedUseCase: IsOnboardingPassedUseCase,
) : ViewModel() {

    val startDestination: NavKey = if (isOnboardingPassedUseCase()) Login else Onboarding
}
