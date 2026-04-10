package fr.mrantoine.franji.storage

import android.content.Context
import IP_ADDRESS
import PORT
import client
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

@Serializable
data class User(
    val username: String = "",
    val email: String = "",
    val password: String = "",
)

@Serializable
data class LoginRequest(
    val email: String,
    val password: String
)

@Serializable
data class LoginResponse(
    val message: String,
    val success: Boolean,
    val access_token: String? = null,
    val token_type: String? = null
)

@Serializable
data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String
)

@Serializable
data class RegisterResponse(
    val message: String,
    val success: Boolean
)


@Serializable
data class ErrorResponse(
    val detail: String
)


object UserStorage {
    suspend fun login(email: String, password: String): LoginResponse {
        return try {
            client.post("http://$IP_ADDRESS:$PORT/user/login") {
                contentType(ContentType.Application.Json)
                setBody(LoginRequest(email, password))
            }.body()
        } catch (e: ClientRequestException) {
            val errorBody = e.response.bodyAsText()
            LoginResponse(
                message = errorBody,
                success = false
            )
        } catch (e: Exception) {
            e.printStackTrace()
            LoginResponse(
                message = "Network error",
                success = false
            )
        }
    }

    suspend fun register(username: String, email: String, password: String): RegisterResponse {
        return try {
            client.post("http://$IP_ADDRESS:$PORT/user/register") {
                contentType(ContentType.Application.Json)
                setBody(
                    RegisterRequest(
                        username = username,
                        email = email,
                        password = password
                    )
                )
            }.body()
        } catch (e: ClientRequestException) {
            val errorBody = e.response.bodyAsText()
            RegisterResponse(
                message = errorBody,
                success = false
            )

        } catch (e: Exception) {
            e.printStackTrace()

            RegisterResponse(
                message = "Network error",
                success = false
            )
        }
    }

    private const val TOKEN_FILE = "jwt_token.txt"

    fun saveToken(context: Context, token: String) {
        val file = File(context.filesDir, TOKEN_FILE)
        file.writeText(token)
    }

    fun loadToken(context: Context): String? {
        val file = File(context.filesDir, TOKEN_FILE)
        return if (file.exists()) file.readText() else null
    }

    fun removeToken(context: Context) {
        val file = File(context.filesDir, TOKEN_FILE)
        if (file.exists()) {
            file.delete()
        }
    }
}