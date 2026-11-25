package ua.edu.chnu.kkn.smachno.data.recipes.service

import ua.edu.chnu.kkn.smachno.data.common.Result
import ua.edu.chnu.kkn.smachno.data.recipes.model.RecipesResponse

internal const val RECIPES = "/auth/recipes"

internal interface RecipeApiService {
    /** Одна сторінка списку: limit - скільки взяти, skip - скільки пропустити. */
    suspend fun recipes(limit: Int, skip: Int): Result<RecipesResponse>
}
