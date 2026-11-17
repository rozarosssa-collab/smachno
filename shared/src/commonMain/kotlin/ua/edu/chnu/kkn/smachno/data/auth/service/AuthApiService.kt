package ua.edu.chnu.kkn.smachno.data.auth.service

import ua.edu.chnu.kkn.smachno.data.auth.model.response.LoginResponse
import ua.edu.chnu.kkn.smachno.data.auth.model.response.RefreshResponse
import ua.edu.chnu.kkn.smachno.data.common.Result

internal const val BASE_URL = "https://dummyjson.com"
internal const val AUTH = "/auth"
internal const val LOGIN = "/login"
internal const val REFRESH = "/refresh"

internal interface AuthApiService {
    suspend fun login(username: String, password: String): Result<LoginResponse>
    suspend fun refresh(refreshToken: String): Result<RefreshResponse>
}
