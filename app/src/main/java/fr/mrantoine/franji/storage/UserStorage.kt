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
    val user: User
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
    val user: User
)


object UserStorage {
    private var getVocabByIdCache = mutableMapOf<String, Vocab>()


    suspend fun login(email: String, password: String): LoginResponse {
        return try {
            client.post("http://$IP_ADDRESS:$PORT/user/login") {
                contentType(ContentType.Application.Json)
                setBody(LoginRequest(email, password))
            }.body()
        } catch (e: Exception) {
            e.printStackTrace()
            LoginResponse(
                message = "Bad Request",
                user = User()
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
                user = User()
            )
        } catch (e: Exception) {
            e.printStackTrace()
            RegisterResponse(
                message = "Bad Request",
                user = User()
            )
        }
    }



}