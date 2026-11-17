package ua.edu.chnu.kkn.smachno.domain.auth

import ua.edu.chnu.kkn.smachno.data.auth.model.response.RefreshResponse
import ua.edu.chnu.kkn.smachno.data.common.Result

internal interface AuthRepository {
    suspend fun login(username: String, password: String): Result<Unit>
    suspend fun refresh(refreshToken: String): Result<RefreshResponse>
    suspend fun isSignedIn(): Boolean
    suspend fun signedInUsername(): String
    suspend fun signOut()
}
