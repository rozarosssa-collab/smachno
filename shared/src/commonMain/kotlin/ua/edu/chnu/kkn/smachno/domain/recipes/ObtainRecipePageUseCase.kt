package ua.edu.chnu.kkn.smachno.domain.recipes

import ua.edu.chnu.kkn.smachno.data.common.Result
import ua.edu.chnu.kkn.smachno.data.recipes.model.RecipePage

/** Завантажує рівно одну сторінку рецептів через репозиторій. */
internal class ObtainRecipePageUseCase(
    private val recipeRepository: RecipeRepository,
) {
    suspend operator fun invoke(limit: Int, skip: Int): Result<RecipePage> =
        recipeRepository.recipes(limit = limit, skip = skip)
}
