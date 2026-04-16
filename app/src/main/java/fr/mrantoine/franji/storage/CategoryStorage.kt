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
    val keys: Map<String, Array<String>>,
    val categories: Map<String, Array<String>>



)
object CategoryStorage {
    private val keysCache = mutableMapOf<String, Array<String>>()
    suspend fun getKeys(prefix: String = ""): Array<String> {
        keysCache[prefix]?.let {
            return it
        }

        val result = try {
            client.get("$ADDRESS/categories/keys")
            { url { parameters.append("prefix", prefix) } }.body<Array<String>>()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyArray()
        }
        keysCache[prefix] = result
        return result
    }

    private val categoryByPathCache = mutableMapOf<String, Array<String>>()
    suspend fun getCategoryByPath(path: String): Array<String> {
        categoryByPathCache[path]?.let {
            return it
        }

        val result = try {
            client.get("$ADDRESS/categories/path")
            { url { parameters.append("path", path) } }.body<Array<String>>()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyArray()
        }
        categoryByPathCache[path] = result
        return result
    }


    suspend fun loadAll() {
        if(categoryByPathCache.isEmpty()) {
            val result = try {
                client.get("$ADDRESS/categories").body<Map<String, Array<String>>>()
            } catch (e: Exception) {
                e.printStackTrace()
                emptyMap()
            }
            result.forEach { (key, value) ->
                categoryByPathCache[key] = value
            }
        }
    }

    private val cache_file_path = "categories_cache.json"

    fun clearCache(context: Context) {
        keysCache.clear()
        categoryByPathCache.clear()

        val cacheFile = File(context.filesDir, cache_file_path)
        if (cacheFile.exists()) {
            cacheFile.delete()
        }
    }

    fun saveCache(context: Context) {
        val cacheFile = File(context.filesDir, cache_file_path)

        val serializableCache = CategoryCache(
            keys = keysCache.toMap(),
            categories = categoryByPathCache.toMap()
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

            keysCache.clear()
            keysCache.putAll(loadedCache.keys)

            categoryByPathCache.clear()
            categoryByPathCache.putAll(loadedCache.categories)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}








