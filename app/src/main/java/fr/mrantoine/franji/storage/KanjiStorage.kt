package fr.mrantoine.franji.storage

import android.content.Context
import fr.mrantoine.franji.ui.components.formatKana
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

@Serializable
data class Lecture(
    val fr: List<String> = emptyList(),
    val ON: List<String> = emptyList(),
    val kun: List<String> = emptyList()
)

@Serializable
data class MainLecture(
    val fr: String = "",
    val kana: String = ""
)


@Serializable
data class Kanji(
    val kanji: String = "",
    val main_lecture: MainLecture = MainLecture(),
    val lectures: Lecture = Lecture(),
    val id: String = "",
    val angles: List<Float> = emptyList(),
    val vocab: List<String> = emptyList(),
    val similarity: List<String> = emptyList()
)


@Serializable
data class KanjiCache(
    val kanjiById: Map<String, Kanji>
)

object KanjiStorage {
    private val kanji_path = "kanji.json"

    private var kanjiByIdCache = mutableMapOf<String, Kanji>()
    fun getKanjiById(
        context: Context,
        kanjiId: String
    ): Kanji {
        kanjiByIdCache[kanjiId]?.let { return it }
        val jsonString = context.assets.open(kanji_path)
            .bufferedReader()
            .use { it.readText() }

        val json = Json {
            ignoreUnknownKeys = true
        }

        val list: List<Kanji> =
            json.decodeFromString(jsonString)

        val kanji = list.firstOrNull { it.id == kanjiId } ?: Kanji()
        if (kanji != Kanji()) {
            kanjiByIdCache[kanjiId] = kanji
        }
        return kanji
    }


    fun loadAll(context: Context) {
        if (kanjiByIdCache.isEmpty()) {
            val jsonString = context.assets.open(kanji_path)
                .bufferedReader()
                .use { it.readText() }

            val json = Json {
                ignoreUnknownKeys = true
            }

            val kanjis: List<Kanji> = json.decodeFromString(jsonString)

            kanjis.forEach { kanji ->
                kanjiByIdCache[kanji.id] = kanji
            }
        }
    }


    fun search(text: String): List<Kanji> {
        if (text.isBlank()) return emptyList()

        val query = text.lowercase()

        fun startsWithMatch(kanji: Kanji): Boolean {
            return kanji.kanji.startsWith(query) ||
                    kanji.main_lecture.fr.lowercase().startsWith(query) ||
                    kanji.main_lecture.kana.formatKana().lowercase().startsWith(query) ||
                    kanji.main_lecture.kana.startsWith(query) ||

                    kanji.lectures.fr.any { it.lowercase().startsWith(query) } ||
                    kanji.lectures.ON.any {
                        it.formatKana().lowercase().startsWith(query) ||
                                it.startsWith(query)
                    } ||
                    kanji.lectures.kun.any {
                        it.formatKana().lowercase().startsWith(query) ||
                                it.startsWith(query)
                    } ||
                    kanji.vocab.any { it.lowercase().startsWith(query) }
        }

        fun containsMatch(kanji: Kanji): Boolean {
            return kanji.kanji.contains(query) ||
                    kanji.main_lecture.fr.lowercase().contains(query) ||
                    kanji.main_lecture.kana.formatKana().lowercase().contains(query) ||
                    kanji.main_lecture.kana.contains(query) ||

                    kanji.lectures.fr.any { it.lowercase().contains(query) } ||
                    kanji.lectures.ON.any {
                        it.formatKana().lowercase().contains(query) ||
                                it.contains(query)
                    } ||
                    kanji.lectures.kun.any {
                        it.formatKana().lowercase().contains(query) ||
                                it.contains(query)
                    } ||
                    kanji.vocab.any { it.lowercase().contains(query) }
        }

        fun exactMatch(kanji: Kanji, query: String): Boolean {
            return kanji.kanji == query ||
                    kanji.main_lecture.fr.equals(query, ignoreCase = true) ||
                    kanji.main_lecture.kana.formatKana().equals(query, ignoreCase = true) ||
                    kanji.main_lecture.kana == query ||

                    kanji.lectures.fr.any { it.equals(query, ignoreCase = true) } ||
                    kanji.lectures.ON.any {
                        it.formatKana().equals(query, ignoreCase = true) ||
                                it == query
                    } ||
                    kanji.lectures.kun.any {
                        it.formatKana().equals(query, ignoreCase = true) ||
                                it == query
                    } ||
                    kanji.vocab.any { it.equals(query, ignoreCase = true) }
        }

        return kanjiByIdCache.values
            .filter { containsMatch(it) }
            .sortedWith(
                compareBy<Kanji> { !exactMatch(it, query) }
                    .thenBy { !startsWithMatch(it) }
            )
    }


    fun get9LinkedKanji(context: Context, kanjiId: String, categoryPath: String): List<Kanji> {
        val categoryIds = CategoryStorage.getCategoryByPath(context, categoryPath).toMutableList().filter { it.startsWith("kanji") }
        val resultIds = mutableListOf<String>()

        fun addBlock(id: String) {
            val base = kanjiByIdCache[id] ?: return
            resultIds += id
            resultIds += base.similarity.shuffled().take(2).filter { it !in resultIds }
        }

        addBlock(kanjiId)

        kanjiByIdCache.values
            .filter { it.id !in resultIds }
            .randomOrNull()
            ?.let { addBlock(it.id) }

        repeat(9 - resultIds.size) {
            categoryIds
                .filter { it !in resultIds }
                .randomOrNull()
                ?.let { resultIds.add(it) }
        }
        repeat(9 - resultIds.size) {
            kanjiByIdCache.values
                .filter { it.id !in resultIds }
                .randomOrNull()
                ?.let { resultIds.add(it.id) }
        }


        return resultIds.mapNotNull { kanjiByIdCache[it] }.shuffled()
    }

    fun get4LinkedKanji(context: Context, kanjiId: String, categoryPath: String): List<Kanji> {
        val categoryIds = CategoryStorage.getCategoryByPath(context, categoryPath).toMutableList().filter { it.startsWith("kanji") }
        val resultIds = mutableListOf<String>()

        fun addBlock(id: String) {
            val base = kanjiByIdCache[id] ?: return
            resultIds += id
            resultIds += base.similarity.shuffled().take(1).filter { it !in resultIds }
        }

        addBlock(kanjiId)

        kanjiByIdCache.values
            .filter { it.id !in resultIds }
            .randomOrNull()
            ?.let { addBlock(it.id) }

        repeat(4 - resultIds.size) {
            categoryIds
                .filter { it !in resultIds }
                .randomOrNull()
                ?.let { resultIds.add(it) }
        }
        repeat(4 - resultIds.size) {
            kanjiByIdCache.values
                .filter { it.id !in resultIds }
                .randomOrNull()
                ?.let { resultIds.add(it.id) }
        }

        return resultIds.mapNotNull { kanjiByIdCache[it] }.shuffled()
    }


    private val cache_file_path = "kanji_cache.json"
    fun clearCache(context: Context) {
        kanjiByIdCache.clear()

        val cacheFile = File(context.filesDir, cache_file_path)
        if (cacheFile.exists()) {
            cacheFile.delete()
        }
    }

    fun saveCache(context: Context) {
        val cacheFile = File(context.filesDir, cache_file_path)

        val serializableCache = KanjiCache(
            kanjiById = kanjiByIdCache.toMap()
        )

        val jsonString = Json.encodeToString(serializableCache)
        cacheFile.writeText(jsonString)
    }

    fun loadCache(context: Context) {
        val cacheFile = File(context.filesDir, cache_file_path)
        if (!cacheFile.exists()) return

        try {
            val jsonString = cacheFile.readText()
            val loadedCache = Json.decodeFromString<KanjiCache>(jsonString)

            kanjiByIdCache.clear()
            kanjiByIdCache.putAll(loadedCache.kanjiById)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}