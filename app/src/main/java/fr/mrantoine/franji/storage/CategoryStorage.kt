package fr.mrantoine.franji.storage

import android.content.Context
import androidx.compose.runtime.key
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import kotlin.collections.mutableMapOf
import org.json.JSONObject
import java.time.LocalDate
import java.time.ZoneId

@Serializable
data class CategoryCache(
    val keys: Map<String, Array<String>>,
    val categories: Map<String, Array<String>>,
    val progress: Map<String, ProgressWorld>
)


@Serializable
data class ProgressWorld(
    var cards: Map<String, Long>
)


object CategoryStorage {
    private val data_file_path = "categories.json"

    private val progressCache = mutableMapOf<String, ProgressWorld>()


    fun getProgress(
        context: Context,
        path: String,
        fullPath: Boolean = false
    ): Float {
        val keys = getKeys(
            context = context,
            prefix = path,
            exceptTotal = true,
            fullPath = fullPath)
        var total = 0
        var count = 0
        keys.forEach { key ->
            val progress = progressCache[key]
            if(progress != null) {
                count += progress.cards.count { it.value != 0L }
                total += progress.cards.size
            }
        }
        return if(total > 0) count.toFloat() / total.toFloat() else 0f
    }

    private val keysCache = mutableMapOf<String, Array<String>>()
    fun getKeys(
        context: Context,
        prefix: String = "",
        exceptTotal: Boolean = false,
        fullPath: Boolean = false
    ): Array<String> {
        val cacheKey = "$prefix|$exceptTotal|$fullPath"

        keysCache[cacheKey]?.let { return it }

        val jsonString = context.assets
            .open(data_file_path)
            .bufferedReader()
            .use { it.readText() }

        val jsonObject = JSONObject(jsonString)

        val keys = if (fullPath) {
            jsonObject.keys()
                .asSequence()
                .filter { it == prefix }
                .toList()
                .toTypedArray()
        } else {
            jsonObject.keys()
                .asSequence()
                .filter { it.startsWith(prefix) }
                .filter { !exceptTotal || !it.contains("Total") }
                .toList()
                .toTypedArray()
        }

        if (keys.isNotEmpty()) {
            keysCache[cacheKey] = keys
            return keys
        }

        return emptyArray()
    }


    fun resetCard(context: Context, path: String, id: String) {
        val world = progressCache[path]
        if (world != null) {
            world.cards = world.cards.toMutableMap().apply {
                put(id, 0L)
            }
        }
        saveCache(context)
    }

    fun updateCard(context: Context, path: String, id: String) {
        val world = progressCache[path]
        if (world != null) {
            val epoch = LocalDate.now()
                .atStartOfDay(ZoneId.systemDefault())
                .toEpochSecond()

            world.cards = world.cards.toMutableMap().apply {
                put(id, epoch)
            }
        }

        saveCache(context)
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
        if(progressCache.isEmpty()) {
            val keys = getKeys(context, "Quizz", true)

            keys.forEach { key ->
                val ids = getCategoryByPath(context, key)
                progressCache[key] = ProgressWorld(
                    cards = ids.associateWith { 0L }
                )

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
            categories = categoryByPathCache.toMap(),
            progress = progressCache.toMap()
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

            progressCache.clear()
            progressCache.putAll(loadedCache.progress)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}








