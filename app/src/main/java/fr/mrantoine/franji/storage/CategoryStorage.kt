package fr.mrantoine.franji.storage

import android.content.Context
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import kotlin.collections.mutableMapOf
import org.json.JSONObject

@Serializable
data class CategoryCache(
    val keys: Map<String, Array<String>>,
    val categories: Map<String, Array<String>>



)
object CategoryStorage {
    private val data_file_path = "categories.json"
    private val keysCache = mutableMapOf<String, Array<String>>()
    fun getKeys(
        context: Context,
        prefix: String = ""
    ): Array<String> {
        keysCache[prefix]?.let { return it }
        val jsonString = context.assets
            .open(data_file_path)
            .bufferedReader()
            .use { it.readText() }

        val jsonObject = JSONObject(jsonString)


        val keys = jsonObject.keys()
            .asSequence()
            .filter { it.startsWith(prefix) }
            .toList()
            .toTypedArray()

        if (keys.isNotEmpty()) {
            keysCache[prefix] = keys
            return keysCache[prefix]!!
        }
        return emptyArray()
    }

    private val categoryByPathCache = mutableMapOf<String, Array<String>>()
    fun getCategoryByPath(
        context: Context,
        path: String
    ): Array<String> {
        categoryByPathCache[path]?.let { return it }
        val jsonString = context.assets.open(data_file_path)
            .bufferedReader()
            .use { it.readText() }

        val map: Map<String, List<String>> =
            Json.decodeFromString(jsonString)

        map[path]?.let {
            categoryByPathCache[path] = it.toTypedArray()
            return categoryByPathCache[path]!!
        }
        return emptyArray()
    }

    fun loadAll(context: Context) {
        if(categoryByPathCache.isEmpty()) {
            val jsonString = context.assets.open(data_file_path)
                .bufferedReader()
                .use { it.readText() }

            val map: Map<String, List<String>> =
                Json.decodeFromString(jsonString)

            val result: Map<String, Array<String>> =
                map.mapValues { it.value.toTypedArray() }

            getKeys(context)
            getKeys(context, "Kanji")
            getKeys(context, "Grammar")
            getKeys(context, "Kana")
            getKeys(context, "Vocab")
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








