package ua.edu.chnu.kkn.smachno.data.settings

import com.russhwolf.settings.Settings
import ua.edu.chnu.kkn.smachno.core.logging.AppLogger

/**
 * Налаштування користувача. Поки тут лише ознака того, що онбордінг
 * пройдено; бібліотека multiplatform-settings сама обирає сховище
 * під кожну платформу.
 */
class AppSettings(private val settings: Settings = Settings()) {

    fun isOnboardingPassed(): Boolean = settings.getBoolean(KEY_ONBOARDING_PASSED, false)

    fun setOnboardingPassed(passed: Boolean) {
        AppLogger.d { "Зберігаю ознаку проходження онбордінгу: $passed" }
        settings.putBoolean(KEY_ONBOARDING_PASSED, passed)
    }

    private companion object {
        const val KEY_ONBOARDING_PASSED = "onboarding_passed"
    }
}
