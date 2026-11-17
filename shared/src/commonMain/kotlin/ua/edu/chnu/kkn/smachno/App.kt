package ua.edu.chnu.kkn.smachno

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import org.koin.compose.viewmodel.koinViewModel
import ua.edu.chnu.kkn.smachno.ui.login.LoginScreen
import ua.edu.chnu.kkn.smachno.ui.onboarding.OnboardingScreen
import ua.edu.chnu.kkn.smachno.ui.theme.AppTheme

@Serializable
data object Onboarding : NavKey

@Serializable
data object Login : NavKey

/** Щоб стек навігації переживав поворот екрана, ключі треба вміти серіалізувати. */
private val navConfiguration = SavedStateConfiguration {
    serializersModule = SerializersModule {
        polymorphic(NavKey::class) {
            subclass(Onboarding::class, Onboarding.serializer())
            subclass(Login::class, Login.serializer())
        }
    }
}

@Composable
fun App() {
    AppTheme {
        val appViewModel = koinViewModel<AppViewModel>()
        val backStack = rememberNavBackStack(navConfiguration, appViewModel.startDestination)

        NavDisplay(
            backStack = backStack,
            onBack = { backStack.removeLastOrNull() },
            entryDecorators = listOf(
                rememberSaveableStateHolderNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator(),
            ),
            entryProvider = entryProvider {
                entry<Onboarding> {
                    OnboardingScreen(
                        onFinished = {
                            backStack.add(Login)
                            backStack.remove(Onboarding)
                        },
                    )
                }
                entry<Login> {
                    LoginScreen(onSignedIn = { })
                }
            },
        )
    }
}
