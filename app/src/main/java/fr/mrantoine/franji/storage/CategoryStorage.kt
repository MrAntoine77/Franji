package fr.mrantoine.franji.storage

import ADDRESS
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
    val ids: Map<String, Array<String>>
)
object CategoryStorage {
    private val getCategoriesPathsCache = mutableMapOf<String, Array<String>>()
    suspend fun getCategoriesPaths(path: String = ""): Array<String> {
        getCategoriesPathsCache[path]?.let {
            return it
        }

        val result = try {
            val filtered_path = if(path == "") "" else "/$path"
            client.get("$ADDRESS/categories/paths/$path").body<Array<String>>()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyArray()
        }
        getCategoriesPathsCache[path] = result
        return result
    }

    private val getCategoryIdsCache = mutableMapOf<String, Array<String>>()
    suspend fun getCategoryIds(path: String): Array<String> {
        getCategoryIdsCache[path]?.let {
            return it
        }

        val result = try {
            client.get("$ADDRESS/categories/id/$path").body<Array<String>>()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyArray()
        }
        getCategoryIdsCache[path] = result
        return result
    }



    private val cache_file_path = "categories_cache.json"

    fun clearCache(context: Context) {
        getCategoriesPathsCache.clear()
        getCategoryIdsCache.clear()

        val cacheFile = File(context.filesDir, cache_file_path)
        if (cacheFile.exists()) {
            cacheFile.delete()
        }
    }

    fun saveCache(context: Context) {
        val cacheFile = File(context.filesDir, cache_file_path)

        val serializableCache = CategoryCache(
            categories = getCategoriesPathsCache.toMap(),
            ids = getCategoryIdsCache.toMap()
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

            getCategoryIdsCache.clear()
            getCategoryIdsCache.putAll(loadedCache.ids)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}








