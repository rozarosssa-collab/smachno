package ua.edu.chnu.kkn.smachno.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import smachno.shared.generated.resources.Res
import smachno.shared.generated.resources.home_detail_back
import smachno.shared.generated.resources.home_detail_calories
import smachno.shared.generated.resources.home_detail_cook
import smachno.shared.generated.resources.home_detail_empty
import smachno.shared.generated.resources.home_detail_ingredients
import smachno.shared.generated.resources.home_detail_prep
import smachno.shared.generated.resources.home_detail_rating
import smachno.shared.generated.resources.home_detail_servings
import smachno.shared.generated.resources.home_detail_steps
import smachno.shared.generated.resources.home_detail_tags
import smachno.shared.generated.resources.home_recipe_meta
import ua.edu.chnu.kkn.smachno.data.recipes.model.Recipe

/**
 * Права панель розмітки "список - деталі". На вузькому вікні вона ж
 * відкривається окремим екраном, тому має власну кнопку повернення.
 */
@Composable
internal fun RecipeDetailPane(
    recipe: Recipe?,
    canNavigateBack: Boolean,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.surfaceContainerLow,
    ) {
        if (recipe == null) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = stringResource(Res.string.home_detail_empty),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(24.dp),
                )
            }
            return@Surface
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            if (canNavigateBack) {
                TextButton(onClick = onBack) {
                    Text(stringResource(Res.string.home_detail_back))
                }
            }

            Text(
                text = recipe.name,
                style = MaterialTheme.typography.headlineSmall,
            )
            Text(
                text = stringResource(Res.string.home_recipe_meta, recipe.cuisine, recipe.difficulty),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                text = stringResource(
                    Res.string.home_detail_rating,
                    recipe.rating.toString(),
                    recipe.reviewCount,
                ),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Text(
                    text = stringResource(Res.string.home_detail_prep, recipe.prepTimeMinutes),
                    style = MaterialTheme.typography.bodySmall,
                )
                Text(
                    text = stringResource(Res.string.home_detail_cook, recipe.cookTimeMinutes),
                    style = MaterialTheme.typography.bodySmall,
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Text(
                    text = stringResource(Res.string.home_detail_servings, recipe.servings),
                    style = MaterialTheme.typography.bodySmall,
                )
                Text(
                    text = stringResource(Res.string.home_detail_calories, recipe.caloriesPerServing),
                    style = MaterialTheme.typography.bodySmall,
                )
            }

            if (recipe.ingredients.isNotEmpty()) {
                DetailSection(title = stringResource(Res.string.home_detail_ingredients)) {
                    recipe.ingredients.forEach { ingredient ->
                        Text(
                            text = "• $ingredient",
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                }
            }

            if (recipe.instructions.isNotEmpty()) {
                DetailSection(title = stringResource(Res.string.home_detail_steps)) {
                    recipe.instructions.forEachIndexed { index, step ->
                        Text(
                            text = "${index + 1}. $step",
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                }
            }

            val labels = recipe.tags + recipe.mealType
            if (labels.isNotEmpty()) {
                DetailSection(title = stringResource(Res.string.home_detail_tags)) {
                    Text(
                        text = labels.joinToString(" · "),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
    }
}

@Composable
private fun DetailSection(title: String, content: @Composable () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.padding(top = 8.dp),
        )
        content()
    }
}
