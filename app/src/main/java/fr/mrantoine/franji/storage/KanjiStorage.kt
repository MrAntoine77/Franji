package fr.mrantoine.franji.storage

import android.content.Context
import IP_ADDRESS
import PORT
import client
import io.ktor.client.call.body
import io.ktor.client.request.get
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
data class Kanji(
    val kanji: String = "",
    val lectures: List<Lecture> = emptyList(),
    val id: String = "",
    val angles: List<Float> = emptyList(),
    val vocab: List<String> = emptyList()
)

@Serializable
data class KanjiCache(
    val byId: Map<String, Kanji>,
    val byChar: Map<String, Kanji>,
    val lottie: Map<String, String>
)

object KanjiStorage {
    private var getKanjiByIdCache = mutableMapOf<String, Kanji>()
    private var getKanjiByCharCache = mutableMapOf<String, Kanji>()
    private var getLottieByKanjiIdCache = mutableMapOf<String, String>()

    suspend fun getKanjiById(kanjiId: String): Kanji {
        getKanjiByIdCache[kanjiId]?.let { return it }
        val result = try {
            client.get("http://$IP_ADDRESS:$PORT/kanji/kanji_data/id") {
                url { parameters.append("kanji_id", kanjiId) }
            }.body()
        } catch (e: Exception) {
            e.printStackTrace()
            Kanji()
        }
        getKanjiByIdCache[kanjiId] = result
        return result
    }

    suspend fun getKanjiByChar(kanjiChar: String): Kanji {
        getKanjiByCharCache[kanjiChar]?.let { return it }
        val result = try {
            client.get("http://$IP_ADDRESS:$PORT/kanji/kanji_data/char") {
                url { parameters.append("kanji_char", kanjiChar) }
            }.body()
        } catch (e: Exception) {
            e.printStackTrace()
            Kanji()
        }
        getKanjiByCharCache[kanjiChar] = result
        return result
    }

    suspend fun getLottieByKanjiId(kanji_id: String): String {
        getLottieByKanjiIdCache[kanji_id]?.let { return it }

        val result = try {
            client.get("http://$IP_ADDRESS:$PORT/lottie/$kanji_id")
                .body<String>()
                .trimIndent()
        } catch (e: Exception) {
            e.printStackTrace()
            "{}"
        }
        getLottieByKanjiIdCache[kanji_id] = result
        return result
    }

    private val cache_file_path = "kanji_cache.json"
    fun clearCache(context: Context) {
        getKanjiByIdCache.clear()
        getKanjiByCharCache.clear()
        getLottieByKanjiIdCache.clear()

        val cacheFile = File(context.filesDir, cache_file_path)
        if (cacheFile.exists()) {
            cacheFile.delete()
        }
    }
    fun saveCache(context: Context) {
        val cacheFile = File(context.filesDir, cache_file_path)

        val serializableCache = KanjiCache(
            byId = getKanjiByIdCache.toMap(),
            byChar = getKanjiByCharCache.toMap(),
            lottie = getLottieByKanjiIdCache.toMap()
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

            getKanjiByIdCache.clear()
            getKanjiByIdCache.putAll(loadedCache.byId)

            getKanjiByCharCache.clear()
            getKanjiByCharCache.putAll(loadedCache.byChar)

            getLottieByKanjiIdCache.clear()
            getLottieByKanjiIdCache.putAll(loadedCache.lottie)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}