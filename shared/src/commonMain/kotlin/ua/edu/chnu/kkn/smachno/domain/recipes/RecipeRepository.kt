package ua.edu.chnu.kkn.smachno.domain.recipes

import ua.edu.chnu.kkn.smachno.data.common.Result
import ua.edu.chnu.kkn.smachno.data.recipes.model.RecipePage

internal interface RecipeRepository {
    suspend fun recipes(limit: Int, skip: Int): Result<RecipePage>
}
