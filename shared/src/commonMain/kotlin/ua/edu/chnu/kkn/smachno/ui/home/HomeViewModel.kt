package ua.edu.chnu.kkn.smachno.ui.home

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ua.edu.chnu.kkn.smachno.data.recipes.model.Recipe
import ua.edu.chnu.kkn.smachno.domain.auth.SignOutUseCase
import ua.edu.chnu.kkn.smachno.domain.auth.SignedInUsernameUseCase
import ua.edu.chnu.kkn.smachno.domain.recipes.ObtainRecipePageUseCase

/** Скільки рецептів довантажується за один крок гортання. */
internal const val RECIPES_PAGE_SIZE = 10

/** Перша порція більша, щоб одразу заповнити екран планшета чи десктопа. */
private const val INITIAL_LOAD_SIZE = 20

/** За скільки елементів до кінця починати довантаження. */
private const val PREFETCH_DISTANCE = 4

@Stable
internal class HomeViewModel(
    private val obtainRecipePageUseCase: ObtainRecipePageUseCase,
    private val signedInUsernameUseCase: SignedInUsernameUseCase,
    private val signOutUseCase: SignOutUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    private val _events = Channel<HomeEvent>(capacity = Channel.BUFFERED)
    val events: Flow<HomeEvent> = _events.receiveAsFlow()

    /**
     * Фабрика щоразу створює новий PagingSource - без цього refresh() не мав би
     * що перечитувати. cachedIn тримає вже завантажені сторінки в межах
     * життя моделі, тому поворот екрана не змушує качати список заново.
     */
    val recipes: Flow<PagingData<Recipe>> = Pager(
        config = PagingConfig(
            pageSize = RECIPES_PAGE_SIZE,
            initialLoadSize = INITIAL_LOAD_SIZE,
            prefetchDistance = PREFETCH_DISTANCE,
            enablePlaceholders = false,
        ),
        pagingSourceFactory = {
            RecipePagingSource(
                obtainRecipePage = obtainRecipePageUseCase,
                pageSize = RECIPES_PAGE_SIZE,
            )
        },
    ).flow.cachedIn(viewModelScope)

    init {
        viewModelScope.launch {
            _state.update { it.copy(username = signedInUsernameUseCase()) }
        }
    }

    fun onAction(action: HomeAction) {
        when (action) {
            HomeAction.SignOut -> viewModelScope.launch {
                signOutUseCase()
                _events.trySend(HomeEvent.SignedOut)
            }
        }
    }
}
