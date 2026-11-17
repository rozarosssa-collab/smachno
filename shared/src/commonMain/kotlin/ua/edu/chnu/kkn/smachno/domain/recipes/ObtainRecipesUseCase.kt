package ua.edu.chnu.kkn.smachno.domain.recipes

import ua.edu.chnu.kkn.smachno.data.common.Result
import ua.edu.chnu.kkn.smachno.data.recipes.model.Recipe

private const val DEFAULT_LIMIT = 30

/** Завантажує список рецептів для домашнього екрана. */
internal class ObtainRecipesUseCase(
    private val recipeRepository: RecipeRepository,
) {
    suspend operator fun invoke(limit: Int = DEFAULT_LIMIT): Result<List<Recipe>> =
        recipeRepository.recipes(limit)
}
