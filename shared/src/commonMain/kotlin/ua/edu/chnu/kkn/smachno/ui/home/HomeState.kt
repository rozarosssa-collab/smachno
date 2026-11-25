package ua.edu.chnu.kkn.smachno.ui.home

import androidx.compose.runtime.Immutable

/**
 * Сам список рецептів у стані більше не лежить: ним керує Paging і віддає
 * його окремим потоком. Тут лишається те, що до пагінації не стосується.
 */
@Immutable
internal data class HomeState(
    val username: String = "",
)

internal sealed interface HomeAction {
    data object SignOut : HomeAction
}

internal sealed interface HomeEvent {
    data object SignedOut : HomeEvent
}
