package ua.edu.chnu.kkn.smachno.data.common

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

/** Власний результат запиту: або дані, або текст помилки. */
sealed interface Result<out T> {
    data class Success<T>(val data: T) : Result<T>
    data class Failure(val errorMessage: String) : Result<Nothing>
}

inline fun <T> Result<T>.onSuccess(action: (T) -> Unit): Result<T> {
    if (this is Result.Success) action(data)
    return this
}

inline fun <T> Result<T>.onFailure(action: (String) -> Unit): Result<T> {
    if (this is Result.Failure) action(errorMessage)
    return this
}

/** Перетворює успішне значення, помилку лишає як є. */
inline fun <T, R> Result<T>.map(transform: (T) -> R): Result<R> = when (this) {
    is Result.Success -> Result.Success(transform(data))
    is Result.Failure -> Result.Failure(errorMessage)
}

suspend inline fun <reified T> HttpResponse.toResult(): Result<T> = when (status.value) {
    in 200..299 -> try {
        Result.Success(if (T::class == Unit::class) Unit as T else body<T>())
    } catch (e: SerializationException) {
        Result.Failure("Не вдалось розібрати відповідь сервера: ${e.message}")
    }
    else -> Result.Failure(serverMessage(bodyAsText()) ?: "Помилка ${status.value}")
}

/** dummyjson повертає помилку як {"message": "..."}, дістаємо звідти текст. */
fun serverMessage(body: String): String? = try {
    Json.parseToJsonElement(body).jsonObject["message"]?.jsonPrimitive?.content
} catch (e: Exception) {
    null
}

/** Обгортка, щоб мережеві винятки не вилітали далі рівня даних. */
suspend inline fun <reified T> HttpClient.safeRequest(
    crossinline block: suspend HttpClient.() -> HttpResponse,
): Result<T> = try {
    block().toResult<T>()
} catch (e: Exception) {
    Result.Failure("Немає зв'язку з сервером: ${e.message}")
}
