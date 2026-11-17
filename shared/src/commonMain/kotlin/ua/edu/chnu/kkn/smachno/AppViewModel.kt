package ua.edu.chnu.kkn.smachno

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation3.runtime.NavKey
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ua.edu.chnu.kkn.smachno.core.logging.AppLogger
import ua.edu.chnu.kkn.smachno.domain.auth.IsSignedInUseCase
import ua.edu.chnu.kkn.smachno.domain.onboarding.IsOnboardingPassedUseCase

/**
 * Обирає, з якого екрана починати:
 * є токен - одразу домашній, онбордінг пройдено - вхід, інакше онбордінг.
 * Токен лежить у зашифрованому сховищі, тому читаємо його в корутині.
 */
@Stable
internal class AppViewModel(
    private val isOnboardingPassedUseCase: IsOnboardingPassedUseCase,
    private val isSignedInUseCase: IsSignedInUseCase,
) : ViewModel() {

    private val _startDestination = MutableStateFlow<NavKey?>(null)
    val startDestination: StateFlow<NavKey?> = _startDestination.asStateFlow()

    init {
        viewModelScope.launch {
            val destination = when {
                isSignedInUseCase() -> Home
                isOnboardingPassedUseCase() -> Login
                else -> Onboarding
            }
            AppLogger.i { "Стартовий екран: $destination" }
            _startDestination.value = destination
        }
    }
}
