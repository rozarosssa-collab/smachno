package ua.edu.chnu.kkn.smachno.data.recipes.model

import kotlinx.serialization.Serializable

@Serializable
data class Recipe(
    val id: Int,
    val name: String,
    val cuisine: String = "",
    val difficulty: String = "",
    val prepTimeMinutes: Int = 0,
    val cookTimeMinutes: Int = 0,
    val servings: Int = 0,
    val caloriesPerServing: Int = 0,
    val rating: Double = 0.0,
    val tags: List<String> = emptyList(),
    val ingredients: List<String> = emptyList(),
    val instructions: List<String> = emptyList(),
    val mealType: List<String> = emptyList(),
    val reviewCount: Int = 0,
) {
    /** Загальний час готування: підготовка плюс сама готовка. */
    val totalTimeMinutes: Int get() = prepTimeMinutes + cookTimeMinutes
}
