package ua.edu.chnu.kkn.smachno.domain

/** Доступ до налаштувань користувача з галузевого рівня. */
interface PreferencesRepository {
    fun isOnboardingPassed(): Boolean
    fun setOnboardingPassed(passed: Boolean)
}
