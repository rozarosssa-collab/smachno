package ua.edu.chnu.kkn.smachno.data.recipes

import ua.edu.chnu.kkn.smachno.core.logging.AppLogger
import ua.edu.chnu.kkn.smachno.data.common.Result
import ua.edu.chnu.kkn.smachno.data.common.map
import ua.edu.chnu.kkn.smachno.data.recipes.model.RecipePage
import ua.edu.chnu.kkn.smachno.data.recipes.service.RecipeApiService
import ua.edu.chnu.kkn.smachno.domain.recipes.RecipeRepository

internal class AppRecipeRepository(
    private val recipeApiService: RecipeApiService,
) : RecipeRepository {

    override suspend fun recipes(limit: Int, skip: Int): Result<RecipePage> =
        recipeApiService.recipes(limit = limit, skip = skip).map { response ->
            val loaded = skip + response.recipes.size
            AppLogger.d { "Сторінка рецептів: skip=$skip, прийшло ${response.recipes.size} із ${response.total}" }
            RecipePage(
                recipes = response.recipes,
                total = response.total,
                // Порожня відповідь теж означає кінець, інакше можна зациклитись.
                nextSkip = if (response.recipes.isNotEmpty() && loaded < response.total) loaded else null,
            )
        }
}
