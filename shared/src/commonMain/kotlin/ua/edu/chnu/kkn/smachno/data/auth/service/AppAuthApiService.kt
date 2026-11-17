package ua.edu.chnu.kkn.smachno.data.auth.service

import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.authProvider
import io.ktor.client.plugins.auth.providers.BearerAuthProvider
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import ua.edu.chnu.kkn.smachno.data.auth.model.request.LoginRequest
import ua.edu.chnu.kkn.smachno.data.auth.model.request.RefreshRequest
import ua.edu.chnu.kkn.smachno.data.auth.model.response.LoginResponse
import ua.edu.chnu.kkn.smachno.data.auth.model.response.RefreshResponse
import ua.edu.chnu.kkn.smachno.data.common.Result
import ua.edu.chnu.kkn.smachno.data.common.onSuccess
import ua.edu.chnu.kkn.smachno.data.common.safeRequest

internal class AppAuthApiService(
    private val client: HttpClient,
) : AuthApiService {

    override suspend fun login(username: String, password: String): Result<LoginResponse> =
        client.safeRequest<LoginResponse> {
            post("$BASE_URL$AUTH$LOGIN") {
                contentType(ContentType.Application.Json)
                setBody(LoginRequest(username, password))
            }
        }.onSuccess {
            // Старий токен у плагіні вже не актуальний, інакше наступний
            // запит піде зі старим заголовком Authorization.
            client.authProvider<BearerAuthProvider>()?.clearToken()
        }

    override suspend fun refresh(refreshToken: String): Result<RefreshResponse> =
        client.safeRequest {
            post("$BASE_URL$AUTH$REFRESH") {
                contentType(ContentType.Application.Json)
                setBody(RefreshRequest(refreshToken))
            }
        }
}
