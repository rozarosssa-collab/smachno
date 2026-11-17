package ua.edu.chnu.kkn.smachno

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.stringResource
import smachno.shared.generated.resources.Res
import smachno.shared.generated.resources.app_name
import ua.edu.chnu.kkn.smachno.data.settings.AppSettings
import ua.edu.chnu.kkn.smachno.ui.onboarding.OnboardingScreen
import ua.edu.chnu.kkn.smachno.ui.theme.AppTheme

@Composable
fun App() {
    val settings = remember { AppSettings() }
    var onboardingPassed by remember { mutableStateOf(settings.isOnboardingPassed()) }

    AppTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
        ) {
            if (onboardingPassed) {
                // Решта застосунку з'явиться в наступних лабораторних.
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = stringResource(Res.string.app_name),
                        style = MaterialTheme.typography.displayMedium,
                    )
                }
            } else {
                OnboardingScreen(
                    onFinished = {
                        settings.setOnboardingPassed(true)
                        onboardingPassed = true
                    },
                )
            }
        }
    }
}
