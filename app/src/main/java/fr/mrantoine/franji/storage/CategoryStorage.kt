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
        prefix: String = "",
        exceptTotal: Boolean = false
    ): Array<String> {
        val cacheKey = "$prefix|$exceptTotal"

        keysCache[cacheKey]?.let { return it }

        val jsonString = context.assets
            .open(data_file_path)
            .bufferedReader()
            .use { it.readText() }

        val jsonObject = JSONObject(jsonString)

        val keys = jsonObject.keys()
            .asSequence()
            .filter { it.startsWith(prefix) }
            .filter { !exceptTotal || !it.contains("Total") }
            .toList()
            .toTypedArray()

        if (keys.isNotEmpty()) {
            keysCache[cacheKey] = keys
            return keys
        }

        return emptyArray()
    }


    fun getWorlds(
        context: Context,
        prefix: String = ""
    ): Array<String> {
        val jsonString = context.assets
            .open(data_file_path)
            .bufferedReader()
            .use { it.readText() }

        val jsonObject = JSONObject(jsonString)

        val result = jsonObject.keys()
            .asSequence()
            .filter { it.startsWith(prefix) }
            .map { key ->
                val parts = key.split("/")
                if (parts.size >= 2) "${parts[0]}/${parts[1]}" else key
            }
            .distinct()
            .toList()
            .toTypedArray()

        return result
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


    fun getFirsts(
        context: Context,
        path: String
    ): Array<String> {
        val all = getCategoryByPath(context, path)

        var firstKanji: String? = null
        var firstVocab: String? = null
        var firstGrammar: String? = null

        for (item in all) {
            when {
                firstKanji == null && item.startsWith("kanji") -> {
                    val first = KanjiStorage.getKanjiById(context, item).kanji
                    firstKanji = if (first.length > 3) {
                        first.take(3) + "…"
                    } else {
                        first
                    }
                }
                firstVocab == null && item.startsWith("vocab") -> {
                    val first = VocabStorage.getVocabById(context, item).jp


                    firstVocab = if (first.length > 3) {
                        first.take(3) + "…"
                    } else {
                        first
                    }
                }
                firstGrammar == null && item.startsWith("grammar") -> {
                    val first = GrammarStorage
                        .getGrammarById(context, item)
                        .examples
                        .firstOrNull()
                        ?.elements
                        ?.jp
                        ?.joinToString(", ")
                        ?: ""
                    firstGrammar = if (first.length > 3) {
                        first.take(3) + "…"
                    } else {
                        first
                    }
                }
            }
            // Si on a trouvé les 3, pas besoin de continuer
            if (firstKanji != null && firstVocab != null && firstGrammar != null) break
        }

        return listOfNotNull(firstKanji, firstVocab, firstGrammar).toTypedArray()
    }

    fun getCountsIds(
        context: Context,
        path: String
    ): Map<String, Int> {
        val all = getCategoryByPath(context, path)

        var kanjiCount = 0
        var vocabCount = 0
        var grammarCount = 0

        for (item in all) {
            when {
                item.startsWith("kanji") -> kanjiCount++
                item.startsWith("vocab") -> vocabCount++
                item.startsWith("grammar") -> grammarCount++
            }
        }

        return mapOf(
            "kanji" to kanjiCount,
            "vocab" to vocabCount,
            "grammar" to grammarCount
        )
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








