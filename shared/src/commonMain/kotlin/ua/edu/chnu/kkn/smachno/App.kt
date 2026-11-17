package ua.edu.chnu.kkn.smachno

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
import ua.edu.chnu.kkn.smachno.ui.home.HomeScreen
import ua.edu.chnu.kkn.smachno.ui.login.LoginScreen
import ua.edu.chnu.kkn.smachno.ui.onboarding.OnboardingScreen
import ua.edu.chnu.kkn.smachno.ui.theme.AppTheme

@Serializable
data object Onboarding : NavKey

@Serializable
data object Login : NavKey

@Serializable
data object Home : NavKey

/** Щоб стек навігації переживав поворот екрана, ключі треба вміти серіалізувати. */
private val navConfiguration = SavedStateConfiguration {
    serializersModule = SerializersModule {
        polymorphic(NavKey::class) {
            subclass(Onboarding::class, Onboarding.serializer())
            subclass(Login::class, Login.serializer())
            subclass(Home::class, Home.serializer())
        }
    }
}

@Composable
fun App() {
    AppTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
        ) {
            val appViewModel = koinViewModel<AppViewModel>()
            val startDestination by appViewModel.startDestination.collectAsStateWithLifecycle()

            when (val start = startDestination) {
                // Поки читаємо токен зі сховища, показуємо індикатор.
                null -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
                else -> AppNavigation(start)
            }
        }
    }
}

@Composable
private fun AppNavigation(startDestination: NavKey) {
    val backStack = rememberNavBackStack(navConfiguration, startDestination)

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
                LoginScreen(
                    onSignedIn = {
                        backStack.add(Home)
                        backStack.remove(Login)
                    },
                )
            }
            entry<Home> {
                HomeScreen(
                    onSignedOut = {
                        backStack.add(Login)
                        backStack.remove(Home)
                    },
                )
            }
        },
    )
}
