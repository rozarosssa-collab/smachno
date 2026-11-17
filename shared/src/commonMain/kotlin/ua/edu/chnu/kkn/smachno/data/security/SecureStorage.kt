package ua.edu.chnu.kkn.smachno.data.security

internal const val ACCESS_TOKEN_KEY = "access_token"
internal const val REFRESH_TOKEN_KEY = "refresh_token"
internal const val USERNAME_KEY = "username"

/** Безпечне сховище для токенів. Реалізацію дає KSafe. */
internal interface SecureStorage {
    suspend fun getAccessToken(): String
    suspend fun setAccessToken(token: String)
    suspend fun getRefreshToken(): String
    suspend fun setRefreshToken(token: String)
    suspend fun getUsername(): String
    suspend fun setUsername(username: String)
    suspend fun clear()
}
