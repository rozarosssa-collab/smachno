package ua.edu.chnu.kkn.smachno.data.auth

import ua.edu.chnu.kkn.smachno.core.logging.AppLogger
import ua.edu.chnu.kkn.smachno.data.auth.model.response.RefreshResponse
import ua.edu.chnu.kkn.smachno.data.auth.service.AuthApiService
import ua.edu.chnu.kkn.smachno.data.common.Result
import ua.edu.chnu.kkn.smachno.data.common.map
import ua.edu.chnu.kkn.smachno.data.security.SecureStorage
import ua.edu.chnu.kkn.smachno.domain.auth.AuthRepository

internal class AppAuthRepository(
    private val secureStorage: SecureStorage,
    private val authApiService: AuthApiService,
) : AuthRepository {

    override suspend fun login(username: String, password: String): Result<Unit> =
        authApiService.login(username, password).map { response ->
            AppLogger.i { "Вхід виконано, користувач ${response.username}" }
            secureStorage.setAccessToken(response.accessToken)
            secureStorage.setRefreshToken(response.refreshToken)
            secureStorage.setUsername(response.username)
        }

    override suspend fun refresh(refreshToken: String): Result<RefreshResponse> =
        authApiService.refresh(refreshToken)

    override suspend fun isSignedIn(): Boolean = secureStorage.getAccessToken().isNotEmpty()

    override suspend fun signedInUsername(): String = secureStorage.getUsername()

    override suspend fun signOut() {
        AppLogger.i { "Вихід з облікового запису" }
        secureStorage.clear()
    }
}
