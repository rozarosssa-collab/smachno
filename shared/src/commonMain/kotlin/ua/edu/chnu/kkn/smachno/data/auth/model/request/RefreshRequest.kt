package ua.edu.chnu.kkn.smachno.data.auth.model.request

import kotlinx.serialization.Serializable

@Serializable
internal data class RefreshRequest(
    val refreshToken: String,
    val expiresInMins: Int = TOKEN_LIFETIME_MINUTES,
)
