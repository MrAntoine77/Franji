package fr.mrantoine.franji.storage

import android.content.Context
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

@Serializable
data class Lecture(
    val fr: List<String> = emptyList(),
    val ON: List<Pronunciation> = emptyList(),
    val kun: List<Pronunciation> = emptyList()
)

@Serializable
data class Pronunciation(
    val romaji: String = "",
    val kana: String = "",
)

@Serializable
data class MainLecture(
    val fr: String = "",
    val romaji: String = "",
    val kana: String = ""
)



@Serializable
data class Kanji(
    val kanji: String = "",
    val main_lecture: MainLecture = MainLecture(),
    val lectures: Lecture = Lecture(),
    val id: String = "",
    val angles: List<Float> = emptyList(),
    val vocab: List<String> = emptyList()
)



@Serializable
data class KanjiCache(
    val kanjiById: Map<String, Kanji>
)

object KanjiStorage {
    private val data_file_path = "kanji.json"




    private var kanjiByIdCache = mutableMapOf<String, Kanji>()
    fun getKanjiById(
        context: Context,
        kanjiId: String
    ): Kanji {
        kanjiByIdCache[kanjiId]?.let { return it }
        val jsonString = context.assets.open(data_file_path)
            .bufferedReader()
            .use { it.readText() }

        val json = Json {
            ignoreUnknownKeys = true
        }

        val list: List<Kanji> =
            json.decodeFromString(jsonString)

        val kanji = list.firstOrNull { it.id == kanjiId } ?: Kanji()
        if(kanji != Kanji()) {
            kanjiByIdCache[kanjiId] = kanji
        }
        return kanji
    }


    fun loadAllKanji(context: Context) {
        if(kanjiByIdCache.isEmpty()) {
            val jsonString = context.assets.open(data_file_path)
                .bufferedReader()
                .use { it.readText() }

            val json = Json {
                ignoreUnknownKeys = true
            }

            val kanjis: List<Kanji>  = json.decodeFromString(jsonString)

            kanjis.forEach { kanji ->
                kanjiByIdCache[kanji.id] = kanji
            }
        }
    }



    fun loadAll(context: Context) {
        loadAllKanji(context)
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