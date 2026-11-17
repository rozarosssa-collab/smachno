package ua.edu.chnu.kkn.smachno.ui.onboarding

/** Те, що користувач може зробити на екрані онбордінгу. */
internal sealed interface OnboardingAction {
    data object Complete : OnboardingAction
}
