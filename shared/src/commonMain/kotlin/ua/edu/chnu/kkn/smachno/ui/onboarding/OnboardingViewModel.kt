package ua.edu.chnu.kkn.smachno.ui.onboarding

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import ua.edu.chnu.kkn.smachno.core.logging.AppLogger
import ua.edu.chnu.kkn.smachno.domain.onboarding.MarkOnboardingPassedUseCase

@Stable
internal class OnboardingViewModel(
    private val markOnboardingPassedUseCase: MarkOnboardingPassedUseCase,
) : ViewModel() {

    private val _events = Channel<OnboardingEvent>(capacity = Channel.BUFFERED)
    val events: Flow<OnboardingEvent> = _events.receiveAsFlow()

    fun onAction(action: OnboardingAction) {
        when (action) {
            OnboardingAction.Complete -> {
                AppLogger.i { "Онбордінг завершено" }
                markOnboardingPassedUseCase()
                _events.trySend(OnboardingEvent.OnboardingPassed)
            }
        }
    }
}
