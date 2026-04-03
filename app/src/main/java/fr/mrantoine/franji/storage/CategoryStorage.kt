package fr.mrantoine.franji.storage

import IP_ADDRESS
import PORT
import android.content.Context
import client

import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import kotlin.collections.mutableMapOf


@Serializable
data class CategoryCache(
    val categories: Map<String, Array<String>>,
    val kanjiChar: Map<String, Array<String>>,
    val kanjiId: Map<String, Array<String>>
)
object CategoryStorage {
    private val getCategoriesPathsCache = mutableMapOf<String, Array<String>>()
    suspend fun getCategoriesPaths(path: String = ""): Array<String> {
        getCategoriesPathsCache[path]?.let {
            return it
        }

        val result = try {
            val filtered_path = if(path == "") "" else "/$path"
            client.get("http://$IP_ADDRESS:$PORT/categories/paths$filtered_path").body<Array<String>>()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyArray()
        }
        getCategoriesPathsCache[path] = result
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



    private val cache_file_path = "categories_cache.json"

    fun clearCache(context: Context) {
        getCategoriesPathsCache.clear()
        getCategoriesKanjiCharCache.clear()
        getCategoriesKanjiIdCache.clear()

        val cacheFile = File(context.filesDir, cache_file_path)
        if (cacheFile.exists()) {
            cacheFile.delete()
        }
    }

    fun saveCache(context: Context) {
        val cacheFile = File(context.filesDir, cache_file_path)

        val serializableCache = CategoryCache(
            categories = getCategoriesPathsCache.toMap(),
            kanjiChar = getCategoriesKanjiCharCache.toMap(),
            kanjiId = getCategoriesKanjiIdCache.toMap()
        )

        val jsonString = Json.encodeToString(serializableCache)
        cacheFile.writeText(jsonString)
    }

    fun loadCache(context: Context) {
        val cacheFile = File(context.filesDir, cache_file_path)
        if (!cacheFile.exists()) return

        try {
            val jsonString = cacheFile.readText()
            val loadedCache = Json.decodeFromString<CategoryCache>(jsonString)

            getCategoriesPathsCache.clear()
            getCategoriesPathsCache.putAll(loadedCache.categories)

            getCategoriesKanjiCharCache.clear()
            getCategoriesKanjiCharCache.putAll(loadedCache.kanjiChar)

            getCategoriesKanjiIdCache.clear()
            getCategoriesKanjiIdCache.putAll(loadedCache.kanjiId)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}








