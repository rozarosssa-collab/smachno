package ua.edu.chnu.kkn.smachno.data.recipes.model

import kotlinx.serialization.Serializable

@Serializable
internal data class RecipesResponse(
    val recipes: List<Recipe> = emptyList(),
    val total: Int = 0,
)
