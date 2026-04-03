package fr.mrantoine.franji.network

import IP_ADDRESS
import PORT
import client
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject


suspend fun getCategories(path: String = ""): Map<String, Any> {

    return try {
        val response = client.get("http://$IP_ADDRESS:$PORT/categories/$path")
        val jsonString: String = response.body()

        val jsonObject: JsonObject = Json.parseToJsonElement(jsonString).jsonObject
        jsonObject.mapValues { it.value }
    } catch (e: Exception) {
        e.printStackTrace()
        emptyMap()
    }
}


suspend fun getCategoryKanjiChar(path: String): Array<String> {
    return try {
        client.get("http://$IP_ADDRESS:$PORT/categories/category/kanji_char/$path").body()
    } catch (e: Exception) {
        e.printStackTrace()
        emptyArray()
    }
}

suspend fun getCategoryKanjiId(path: String): Array<String> {
    return try {
        client.get("http://$IP_ADDRESS:$PORT/categories/category/kanji_id/$path").body()
    } catch (e: Exception) {
        e.printStackTrace()
        emptyArray()
    }
}

suspend fun getCategoriesPaths(): Array<String> {
    return try {
        client.get("http://$IP_ADDRESS:$PORT/categories/paths").body()
    } catch (e: Exception) {
        e.printStackTrace()
        emptyArray()
    }
}