package ua.edu.chnu.kkn.smachno.data.recipes.service

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import ua.edu.chnu.kkn.smachno.data.auth.service.BASE_URL
import ua.edu.chnu.kkn.smachno.data.common.Result
import ua.edu.chnu.kkn.smachno.data.common.safeRequest
import ua.edu.chnu.kkn.smachno.data.recipes.model.RecipesResponse

/**
 * Список рецептів лежить за захищеною адресою, тому заголовок Authorization
 * сюди підставляє плагін Auth із збереженого токена.
 */
internal class AppRecipeApiService(
    private val client: HttpClient,
) : RecipeApiService {

    override suspend fun recipes(limit: Int): Result<RecipesResponse> =
        client.safeRequest {
            get("$BASE_URL$RECIPES") {
                parameter("limit", limit)
            }
        }
}
