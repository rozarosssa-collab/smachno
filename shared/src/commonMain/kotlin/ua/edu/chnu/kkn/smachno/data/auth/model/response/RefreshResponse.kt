package ua.edu.chnu.kkn.smachno.data.auth.model.response

import kotlinx.serialization.Serializable

@Serializable
internal data class RefreshResponse(
    val accessToken: String,
    val refreshToken: String,
)
