package fr.mrantoine.franji.storage

import android.content.Context
import android.icu.text.CaseMap
import android.util.JsonReader
import androidx.compose.runtime.key
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import kotlin.collections.mutableMapOf
import org.json.JSONObject
import java.io.InputStreamReader
import java.time.LocalDate
import java.time.ZoneId

@Serializable
data class CategoryCache(
    val keys:       Map<String, Array<String>>,
    val categories: Map<String, Array<String>>,
    val progress:   Map<String, ProgressWorld>
)


@Serializable
data class FirstElement(
    val title: String,
    val kanji: String? = null,
    val vocab: String? = null,
    val grammar: String? = null,
    val kanji_count: Int,
    val vocab_count: Int,
    val grammar_count: Int
)



@Serializable
data class ProgressWorld(
    //id, progress
    var cards: Map<String, Long>
)


object CategoryStorage {
    private val categories_path = "categories.json"
    private val worldfirst_path = "worldfirst.json"
    private val levelfirst_path = "levelfirst.json"

    private val progressCache = mutableMapOf<String, ProgressWorld>()


    fun getProgress(
        context: Context,
        path: String,
        fullpath: Boolean = false
    ): Float {
        val keys = if(fullpath) getKeysFullpath(
            context = context,
            path = path,
            )
        else getKeys(
            context = context,
            prefix = path,
        )
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
    ): Array<String> {
        keysCache[prefix]?.let { return it }

        val keys = mutableListOf<String>()

        context.assets.open(categories_path).use { input ->
            JsonReader(InputStreamReader(input, Charsets.UTF_8)).use { reader ->
                reader.beginObject()
                while (reader.hasNext()) {
                    val name = reader.nextName()

                    val matches = name.startsWith(prefix)

                    if (matches) keys.add(name)

                    reader.skipValue()
                }
                reader.endObject()
            }
        }

        val result = keys.toTypedArray()
        if (result.isNotEmpty()) {
            keysCache[prefix] = result
        }
        return result
    }

    fun loadAllQuizzKeys(
        context: Context,
    ) {
        val tempKeys = mutableMapOf<String, MutableList<String>>()

        for (i in 1..20) {
            val prefix = "Quizz/Monde_${i}/"
            tempKeys[prefix] = mutableListOf()
        }

        context.assets.open(categories_path).use { input ->
            JsonReader(InputStreamReader(input, Charsets.UTF_8)).use { reader ->

                reader.beginObject()

                while (reader.hasNext()) {
                    val name = reader.nextName()

                    val prefix = tempKeys.keys.firstOrNull {
                        name.startsWith(it)
                    }

                    if (prefix != null) {
                        tempKeys[prefix]?.add(name)
                    }

                    reader.skipValue()
                }

                reader.endObject()
            }
        }

        // Remplissage du cache
        tempKeys.forEach { (prefix, keys) ->
            if (keys.isNotEmpty()) {
                keysCache[prefix] = keys.toTypedArray()
            }
        }
    }





    fun getKeysFullpath(
        context: Context,
        path: String = "",
    ): Array<String> {

        keysCache[path]?.let { return it }

        val keys = mutableListOf<String>()

        context.assets.open(categories_path).use { input ->
            JsonReader(InputStreamReader(input, Charsets.UTF_8)).use { reader ->

                reader.beginObject()

                while (reader.hasNext()) {
                    val name = reader.nextName()

                    if (name == path) {
                        keys.add(name)
                    }

                    reader.skipValue()
                }

                reader.endObject()
            }
        }

        val result = keys.toTypedArray()

        if (result.isNotEmpty()) {
            keysCache[path] = result
        }

        return result
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
            .open(categories_path)
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
        val jsonString = context.assets.open(categories_path)
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


    fun getWorldfirsts(context: Context): List<FirstElement> {
        val jsonString = context.assets.open(worldfirst_path)
            .bufferedReader()
            .use { it.readText() }

        val map: Map<String, FirstElement> = Json.decodeFromString(jsonString)

        return map.values.toList()
    }

    fun getLevelFirsts(context: Context, world: String): List<FirstElement> {
        val jsonString = context.assets.open(levelfirst_path)
            .bufferedReader()
            .use { it.readText() }

        val map: Map<String, FirstElement> = Json.decodeFromString(jsonString)

        return map.values.filter { it.title.startsWith("$world/") }
    }


    fun loadAll(context: Context) {
        if(categoryByPathCache.isEmpty()) {
            val jsonString = context.assets.open(categories_path)
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
            loadAllQuizzKeys(context)



            result.forEach { (key, value) ->
                categoryByPathCache[key] = value
            }
        }
        if(progressCache.isEmpty()) {
            val keys = getKeys(context, "Quizz")

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








