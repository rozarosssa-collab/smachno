package ua.edu.chnu.kkn.smachno.di

import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.encodedPath
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import ua.edu.chnu.kkn.smachno.core.logging.AppLogger
import ua.edu.chnu.kkn.smachno.data.auth.AppAuthRepository
import ua.edu.chnu.kkn.smachno.data.auth.service.AppAuthApiService
import ua.edu.chnu.kkn.smachno.data.auth.service.AuthApiService
import ua.edu.chnu.kkn.smachno.data.auth.service.LOGIN
import ua.edu.chnu.kkn.smachno.data.auth.service.REFRESH
import ua.edu.chnu.kkn.smachno.data.common.onFailure
import ua.edu.chnu.kkn.smachno.data.common.onSuccess
import ua.edu.chnu.kkn.smachno.data.security.AppSecureStorage
import ua.edu.chnu.kkn.smachno.data.security.SecureStorage
import ua.edu.chnu.kkn.smachno.domain.auth.AuthRepository

/** Сховище токенів. */
internal val secureModule = module {
    includes(platformModule)
    singleOf(::AppSecureStorage) { bind<SecureStorage>() }
}

/** Клієнт Ktor і все, що працює з мережею. */
internal val networkModule = module {
    includes(secureModule)

    single {
        val secureStorage: SecureStorage = get()
        HttpClient {
            install(Logging) {
                level = LogLevel.INFO
                logger = object : io.ktor.client.plugins.logging.Logger {
                    override fun log(message: String) = AppLogger.d { message }
                }
            }
            install(ContentNegotiation) {
                json(
                    Json {
                        isLenient = true
                        ignoreUnknownKeys = true
                    }
                )
            }
            install(Auth) {
                bearer {
                    loadTokens {
                        BearerTokens(
                            accessToken = secureStorage.getAccessToken(),
                            refreshToken = secureStorage.getRefreshToken(),
                        )
                    }
                    // Викликається автоматично, коли сервер повертає 401.
                    refreshTokens {
                        val authRepository: AuthRepository = get()
                        authRepository.refresh(secureStorage.getRefreshToken())
                            .onSuccess { refreshed ->
                                AppLogger.i { "Токен оновлено" }
                                secureStorage.setAccessToken(refreshed.accessToken)
                                secureStorage.setRefreshToken(refreshed.refreshToken)
                            }
                            .onFailure { error ->
                                AppLogger.e { "Не вдалось оновити токен: $error" }
                                secureStorage.clear()
                            }
                        BearerTokens(
                            accessToken = secureStorage.getAccessToken(),
                            refreshToken = secureStorage.getRefreshToken(),
                        )
                    }
                    // На самі запити входу й оновлення заголовок не чіпляємо.
                    sendWithoutRequest { request ->
                        val path = request.url.encodedPath
                        !path.endsWith(LOGIN) && !path.endsWith(REFRESH)
                    }
                }
            }
        }
    }

    singleOf(::AppAuthApiService) { bind<AuthApiService>() }
    singleOf(::AppAuthRepository) { bind<AuthRepository>() }
}
