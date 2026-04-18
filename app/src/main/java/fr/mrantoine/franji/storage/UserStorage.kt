package fr.mrantoine.franji.storage

import ADDRESS
import client
import android.content.Context
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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
data class UserProgress(
    val progress: Float = 0F,
    val timestamp: Long = 0
)

fun decompressUserData(data: String, keys: Array<String>): Map<String, UserProgress> {

    val result = emptyMap<String, UserProgress>().toMutableMap()

    val entries = if (data.contains(";")) {
        data.split(";")
    } else {
        emptyList()
    }

    keys.forEachIndexed { index, path ->

        var ts = 0L
        var progress = 0.0F


        entries.forEach { entry->
            val parts = entry.split("|")
            if(parts[0].toInt() == index) {
                ts = parts[2].toLong()
                progress = parts[1].toFloat()
            }
        }
        val lastPlay: Long = ts

        result[path] =
            UserProgress(
                progress = progress,
                timestamp = lastPlay
            )

    }
    return result
}


fun compressUserData(
    data: Map<String, UserProgress>,
    keys: Array<String>
): String {
    val result = StringBuilder()
    data.forEach { (path, userProgress) ->

        val pathIndex = keys.indexOf(path)
        if (pathIndex == -1) return@forEach

        val progress = userProgress.progress
        if (progress <= 0.01f) return@forEach

        val ts = userProgress.timestamp

        if (result.isNotEmpty()) result.append(";")

        result.append("$pathIndex|$progress|$ts")
    }

    return result.toString()
}



@Serializable
data class UserCache(
    val userDataByKey: Map<String, UserProgress>,
    val token: String
)



object UserStorage {
    private var userDataCache = mutableMapOf<String, UserProgress>()
    private var tokenCache: String? = null

    fun getToken(): String? {
        return tokenCache
    }

    fun setToken(new_token: String) {
        tokenCache = new_token
    }

    suspend fun initUserData() {
        userDataCache.clear()
        val keys = CategoryStorage.getKeys()
        keys.forEach { key ->
            userDataCache[key] = UserProgress()
        }
    }

    suspend fun getUserData(): Map<String, UserProgress> {
        if(userDataCache.isEmpty()) {
            val keys = CategoryStorage.getKeys()
            if(tokenCache == null) return decompressUserData("", keys)
            val data = try {
                client.get("$ADDRESS/user/get_data") {
                    url { parameters.append("token", tokenCache!!) }
                }.body()
            } catch (e: Exception) {
                e.printStackTrace()
                ""
            }
            userDataCache = decompressUserData(data, keys) as MutableMap<String, UserProgress>
        }

        return userDataCache
    }


    fun setUserData(key: String, progress: Float, timestamp: Long) {
        //timestamp format : yyyy/MM/dd-HH:mm:ss
        userDataCache[key] = UserProgress(progress = progress, timestamp = timestamp)
    }

    suspend fun postUserData() {
        val data = compressUserData(userDataCache, CategoryStorage.getKeys())

        try {
            client.post("$ADDRESS/user/update_data") {
                url {
                    parameters.append("token", tokenCache!!)
                    parameters.append("data", data)
                }
            }.body()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun loadUser() {
        getUserData()
    }


    private val cache_file_path = "user_cache.json"
    fun clearCache(context: Context) {
        userDataCache.clear()
        tokenCache = null

        val cacheFile = File(context.filesDir, cache_file_path)
        if (cacheFile.exists()) {
            cacheFile.delete()
        }
    }
    fun saveCache(context: Context) {
        val cacheFile = File(context.filesDir, cache_file_path)

        val serializableCache = UserCache(
            userDataByKey = userDataCache.toMap(),
            token = tokenCache ?: ""
        )

        val jsonString = Json.encodeToString(serializableCache)
        cacheFile.writeText(jsonString)
    }
    fun loadCache(context: Context) {
        val cacheFile = File(context.filesDir, cache_file_path)
        if (!cacheFile.exists()) return

        try {
            val jsonString = cacheFile.readText()
            val loadedCache = Json.decodeFromString<UserCache>(jsonString)

            userDataCache.clear()
            userDataCache.putAll(loadedCache.userDataByKey)

            tokenCache = null
            tokenCache = if(loadedCache.token == "") null else loadedCache.token

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun login(email: String, password: String): LoginResponse {
        val result = try {
            client.post("$ADDRESS/user/login") {
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
        return result
    }

    suspend fun register(username: String, email: String, password: String): RegisterResponse {
        val result = try {
            client.post("$ADDRESS/user/register") {
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
        if(result.success) {
            initUserData()
        }

        return result
    }
}