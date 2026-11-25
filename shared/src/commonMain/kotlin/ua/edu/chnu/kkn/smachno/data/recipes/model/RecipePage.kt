package ua.edu.chnu.kkn.smachno.data.recipes.model

/**
 * Одна сторінка рецептів.
 *
 * Типи Paging сюди свідомо не тягнемо: рівні даних і домену не знають,
 * хто і яким способом гортатиме список. Усе, що їм треба сказати нагору, -
 * це самі рецепти, скільки їх усього та з якого зміщення читати наступну
 * порцію (null - далі порожньо).
 */
internal data class RecipePage(
    val recipes: List<Recipe>,
    val total: Int,
    val nextSkip: Int?,
)
