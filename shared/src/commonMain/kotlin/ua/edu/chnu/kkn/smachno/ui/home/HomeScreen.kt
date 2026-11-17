package ua.edu.chnu.kkn.smachno.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import smachno.shared.generated.resources.Res
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
import ua.edu.chnu.kkn.smachno.ui.common.isWideWindow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HomeScreen(
    onSignedOut: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

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
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            when {
                state.isLoading -> CircularProgressIndicator(Modifier.align(Alignment.Center))

                state.errorMessage != null -> ErrorBlock(
                    message = state.errorMessage.orEmpty(),
                    onRetry = { viewModel.onAction(HomeAction.Reload) },
                    modifier = Modifier.align(Alignment.Center),
                )

                state.recipes.isEmpty() -> Text(
                    text = stringResource(Res.string.home_empty),
                    modifier = Modifier.align(Alignment.Center),
                )

                else -> RecipeGrid(state.recipes)
            }
        }
    }
}

@Composable
private fun RecipeGrid(recipes: List<Recipe>) {
    // На широкому вікні картки стають у два стовпці, на вузькому - в один.
    LazyVerticalGrid(
        columns = GridCells.Fixed(if (isWideWindow()) 2 else 1),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        items(recipes, key = { it.id }) { recipe ->
            RecipeCard(recipe)
        }
    }
}

@Composable
private fun RecipeCard(recipe: Recipe) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
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
            if (recipe.ingredients.isNotEmpty()) {
                Text(
                    text = recipe.ingredients.joinToString(", "),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

@Composable
private fun ErrorBlock(message: String, onRetry: () -> Unit, modifier: Modifier = Modifier) {
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
