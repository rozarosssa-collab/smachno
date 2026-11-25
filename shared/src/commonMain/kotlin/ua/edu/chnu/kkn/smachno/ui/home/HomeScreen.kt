package ua.edu.chnu.kkn.smachno.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import smachno.shared.generated.resources.Res
import smachno.shared.generated.resources.home_append_error
import smachno.shared.generated.resources.home_empty
import smachno.shared.generated.resources.home_greeting
import smachno.shared.generated.resources.home_recipe_calories
import smachno.shared.generated.resources.home_recipe_meta
import smachno.shared.generated.resources.home_recipe_servings
import smachno.shared.generated.resources.home_recipe_time
import smachno.shared.generated.resources.home_retry
import smachno.shared.generated.resources.home_sign_out
import smachno.shared.generated.resources.home_title
import ua.edu.chnu.kkn.smachno.data.recipes.model.Recipe
import ua.edu.chnu.kkn.smachno.ui.common.ObserveEvents

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalMaterial3AdaptiveApi::class,
    ExperimentalComposeUiApi::class,
)
@Composable
internal fun HomeScreen(
    onSignedOut: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val recipes = viewModel.recipes.collectAsLazyPagingItems()

    // Навігатор тримає, який рецепт зараз відкрито, і сам вирішує, показати
    // панелі поруч чи одну поверх одної - залежно від ширини вікна.
    val navigator = rememberListDetailPaneScaffoldNavigator<Int>()
    val scope = rememberCoroutineScope()

    ObserveEvents(viewModel.events) { event ->
        when (event) {
            HomeEvent.SignedOut -> onSignedOut()
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize().safeContentPadding(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = stringResource(Res.string.home_title),
                            style = MaterialTheme.typography.titleLarge,
                        )
                        if (state.username.isNotEmpty()) {
                            Text(
                                text = stringResource(Res.string.home_greeting, state.username),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                },
                actions = {
                    TextButton(onClick = { viewModel.onAction(HomeAction.SignOut) }) {
                        Text(stringResource(Res.string.home_sign_out))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer,
                ),
            )
        },
    ) { innerPadding ->
        val selectedId = navigator.currentDestination?.contentKey
        val canGoBackToList = navigator.canNavigateBack()

        // На вузькому вікні системна кнопка "назад" має закривати деталі,
        // а не весь екран зі списком. BackHandler позначений застарілим на
        // користь NavigationEventHandler, але той тягне окремий стан події
        // навігації, якого тут більше ніде не треба.
        @Suppress("DEPRECATION")
        BackHandler(enabled = canGoBackToList) {
            scope.launch { navigator.navigateBack() }
        }

        ListDetailPaneScaffold(
            navigator.scaffoldDirective,
            navigator.scaffoldState,
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            listPane = {
                AnimatedPane {
                    RecipeListPane(
                        recipes = recipes,
                        selectedId = selectedId,
                        onRecipeClick = { id ->
                            scope.launch {
                                navigator.navigateTo(ListDetailPaneScaffoldRole.Detail, id)
                            }
                        },
                    )
                }
            },
            detailPane = {
                AnimatedPane {
                    // Деталі беремо з уже завантажених сторінок, без окремого запиту.
                    val snapshot = recipes.itemSnapshotList
                    val recipe = remember(selectedId, snapshot) {
                        snapshot.items.firstOrNull { it.id == selectedId }
                    }
                    RecipeDetailPane(
                        recipe = recipe,
                        // Кнопка потрібна лише там, де панелі не вміщаються поруч.
                        canNavigateBack = canGoBackToList,
                        onBack = { scope.launch { navigator.navigateBack() } },
                    )
                }
            },
        )
    }
}

@Composable
private fun RecipeListPane(
    recipes: LazyPagingItems<Recipe>,
    selectedId: Int?,
    onRecipeClick: (Int) -> Unit,
) {
    val refreshState = recipes.loadState.refresh

    Box(modifier = Modifier.fillMaxSize()) {
        when {
            // Перше завантаження або перезавантаження з нуля.
            refreshState is LoadState.Loading -> CircularProgressIndicator(
                Modifier.align(Alignment.Center),
            )

            refreshState is LoadState.Error -> ErrorBlock(
                message = refreshState.error.message.orEmpty(),
                onRetry = recipes::retry,
                modifier = Modifier.align(Alignment.Center),
            )

            recipes.itemCount == 0 -> Text(
                text = stringResource(Res.string.home_empty),
                modifier = Modifier.align(Alignment.Center),
            )

            else -> RecipeGrid(
                recipes = recipes,
                selectedId = selectedId,
                onRecipeClick = onRecipeClick,
            )
        }
    }
}

@Composable
private fun RecipeGrid(
    recipes: LazyPagingItems<Recipe>,
    selectedId: Int?,
    onRecipeClick: (Int) -> Unit,
) {
    // Колонки рахуються від ширини самої панелі, тому сітка однаково
    // доречна і на телефоні, і на половині екрана десктопа.
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 280.dp),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        items(
            count = recipes.itemCount,
            // Стабільний ключ: під час довантаження вже показані картки не перемальовуються.
            key = recipes.itemKey { it.id },
        ) { index ->
            val recipe = recipes[index]
            if (recipe != null) {
                RecipeCard(
                    recipe = recipe,
                    isSelected = recipe.id == selectedId,
                    onClick = { onRecipeClick(recipe.id) },
                )
            }
        }

        // Підвал списку: що відбувається з наступною сторінкою.
        when (val appendState = recipes.loadState.append) {
            is LoadState.Loading -> item(span = { GridItemSpan(maxLineSpan) }) {
                Box(Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(Modifier.size(28.dp))
                }
            }

            is LoadState.Error -> item(span = { GridItemSpan(maxLineSpan) }) {
                ErrorBlock(
                    message = appendState.error.message
                        ?: stringResource(Res.string.home_append_error),
                    onRetry = recipes::retry,
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            is LoadState.NotLoading -> Unit
        }
    }
}

@Composable
private fun RecipeCard(
    recipe: Recipe,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) {
                MaterialTheme.colorScheme.secondaryContainer
            } else {
                MaterialTheme.colorScheme.surfaceContainerHigh
            },
        ),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.Top,
            ) {
                Text(
                    text = recipe.name,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f),
                )
                Text(
                    text = recipe.rating.toString(),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
            Text(
                text = stringResource(Res.string.home_recipe_meta, recipe.cuisine, recipe.difficulty),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                Text(
                    text = stringResource(Res.string.home_recipe_time, recipe.totalTimeMinutes),
                    style = MaterialTheme.typography.bodySmall,
                )
                Text(
                    text = stringResource(Res.string.home_recipe_servings, recipe.servings),
                    style = MaterialTheme.typography.bodySmall,
                )
                Text(
                    text = stringResource(Res.string.home_recipe_calories, recipe.caloriesPerServing),
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        }
    }
}

@Composable
internal fun ErrorBlock(message: String, onRetry: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
        )
        Button(onClick = onRetry) {
            Text(stringResource(Res.string.home_retry))
        }
    }
}
