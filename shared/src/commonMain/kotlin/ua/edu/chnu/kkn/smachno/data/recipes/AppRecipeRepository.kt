package ua.edu.chnu.kkn.smachno.data.recipes

import ua.edu.chnu.kkn.smachno.core.logging.AppLogger
import ua.edu.chnu.kkn.smachno.data.common.Result
import ua.edu.chnu.kkn.smachno.data.common.map
import ua.edu.chnu.kkn.smachno.data.recipes.model.Recipe
import ua.edu.chnu.kkn.smachno.data.recipes.service.RecipeApiService
import ua.edu.chnu.kkn.smachno.domain.recipes.RecipeRepository

internal class AppRecipeRepository(
    private val recipeApiService: RecipeApiService,
) : RecipeRepository {

    override suspend fun recipes(limit: Int): Result<List<Recipe>> =
        recipeApiService.recipes(limit).map { response ->
            AppLogger.d { "Отримано рецептів: ${response.recipes.size}" }
            response.recipes
        }
}
