package ua.edu.chnu.kkn.smachno.domain.recipes

import ua.edu.chnu.kkn.smachno.data.common.Result
import ua.edu.chnu.kkn.smachno.data.recipes.model.Recipe

internal interface RecipeRepository {
    suspend fun recipes(limit: Int): Result<List<Recipe>>
}
