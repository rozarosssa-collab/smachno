package ua.edu.chnu.kkn.smachno.data.preferences

import com.russhwolf.settings.Settings
import com.russhwolf.settings.get
import com.russhwolf.settings.set
import ua.edu.chnu.kkn.smachno.core.logging.AppLogger
import ua.edu.chnu.kkn.smachno.domain.PreferencesRepository

/**
 * Реалізація сховища налаштувань на multiplatform-settings.
 * Галузевий рівень знає тільки про інтерфейс, а не про бібліотеку.
 */
internal class AppPreferencesRepository(
    private val settings: Settings,
) : PreferencesRepository {

    override fun isOnboardingPassed(): Boolean =
        settings.get<Boolean>(PreferenceKey.IS_ONBOARDING_PASSED.key) ?: false

    override fun setOnboardingPassed(passed: Boolean) {
        AppLogger.d { "Зберігаю ознаку проходження онбордінгу: $passed" }
        settings[PreferenceKey.IS_ONBOARDING_PASSED.key] = passed
    }
}
