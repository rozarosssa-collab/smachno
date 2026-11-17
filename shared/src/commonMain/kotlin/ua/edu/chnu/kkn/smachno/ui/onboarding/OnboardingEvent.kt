package ua.edu.chnu.kkn.smachno.ui.onboarding

/** Одноразові події, на які реагує екран. */
internal sealed interface OnboardingEvent {
    data object OnboardingPassed : OnboardingEvent
}
