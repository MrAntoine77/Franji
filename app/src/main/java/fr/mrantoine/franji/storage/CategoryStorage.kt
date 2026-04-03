package fr.mrantoine.franji.storage

import IP_ADDRESS
import PORT
import client
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject


object CategoryStorage {

    private val categoriesCache = mutableMapOf<String, Map<String, Any>>()
    suspend fun getCategories(path: String = ""): Map<String, Any> {
        categoriesCache[path]?.let {
            return it
        }

        val result = try {
            val response = client.get("http://$IP_ADDRESS:$PORT/categories/$path")
            val jsonString: String = response.body()

            val jsonObject: JsonObject = Json.parseToJsonElement(jsonString).jsonObject
            jsonObject.mapValues { it.value }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyMap()
        }

        categoriesCache[path] = result
        return result
    }


    private val getCategoriesKanjiCharCache = mutableMapOf<String, Array<String>>()
    suspend fun getCategoriesKanjiChar(path: String): Array<String> {
        getCategoriesKanjiCharCache[path]?.let {
            return it
        }

        val result = try {
            client.get("http://$IP_ADDRESS:$PORT/categories/category/kanji_char/$path").body<Array<String>>()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyArray()
        }
        getCategoriesKanjiCharCache[path] = result
        return result
    }

    private val getCategoriesKanjiIdCache = mutableMapOf<String, Array<String>>()
    suspend fun getCategoriesKanjiId(path: String): Array<String> {
        getCategoriesKanjiIdCache[path]?.let {
            return it
        }

        val result = try {
            client.get("http://$IP_ADDRESS:$PORT/categories/category/kanji_id/$path").body<Array<String>>()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyArray()
        }
        getCategoriesKanjiIdCache[path] = result
        return result
    }
}








