package ua.edu.chnu.kkn.smachno.ui.home

import androidx.paging.PagingSource
import androidx.paging.PagingState
import ua.edu.chnu.kkn.smachno.data.common.Result
import ua.edu.chnu.kkn.smachno.data.recipes.model.Recipe
import ua.edu.chnu.kkn.smachno.domain.recipes.ObtainRecipePageUseCase

/**
 * Paging носить помилку лише як Throwable, а в нас рівень даних віддає
 * текст у Result.Failure. Загортаємо його сюди, щоб екран показав те саме
 * повідомлення, що й раніше.
 */
internal class RecipeLoadException(override val message: String) : Exception(message)

/**
 * Ключ сторінки - зміщення skip, елемент - рецепт.
 *
 * Джерело живе в шарі представлення: нижче за нього про пагінацію ніхто
 * не знає, use case просто віддає одну сторінку.
 */
internal class RecipePagingSource(
    private val obtainRecipePage: ObtainRecipePageUseCase,
    private val pageSize: Int,
) : PagingSource<Int, Recipe>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Recipe> {
        // Перший запит приходить без ключа - читаємо з початку списку.
        val skip = params.key ?: 0
        return when (val result = obtainRecipePage(limit = params.loadSize, skip = skip)) {
            is Result.Success -> LoadResult.Page(
                data = result.data.recipes,
                prevKey = if (skip == 0) null else (skip - pageSize).coerceAtLeast(0),
                // null прийшов із репозиторію - дані скінчились, довантажень більше не буде.
                nextKey = result.data.nextSkip,
            )

            is Result.Failure -> LoadResult.Error(RecipeLoadException(result.errorMessage))
        }
    }

    /** null означає, що після refresh список читається з першої сторінки. */
    override fun getRefreshKey(state: PagingState<Int, Recipe>): Int? = null
}
