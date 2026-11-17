package ua.edu.chnu.kkn.smachno.ui.home

import androidx.compose.runtime.Immutable
import ua.edu.chnu.kkn.smachno.data.recipes.model.Recipe

@Immutable
internal data class HomeState(
    val username: String = "",
    val isLoading: Boolean = true,
    val recipes: List<Recipe> = emptyList(),
    val errorMessage: String? = null,
)

internal sealed interface HomeAction {
    data object Reload : HomeAction
    data object SignOut : HomeAction
}

internal sealed interface HomeEvent {
    data object SignedOut : HomeEvent
}
