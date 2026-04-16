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
data class Lottie(
    val id: String = "",
    val data: String = "{}"
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
            client.get("$ADDRESS/kanji/kanji_data/id") {
                url { parameters.append("kanji_id", kanjiId) }
            }.body()
        } catch (e: Exception) {
            e.printStackTrace()
            Kanji()
        }
        getKanjiByIdCache[kanjiId] = result
        return result
    }

    suspend fun loadAllKanji() {
        if(getKanjiByIdCache.isEmpty()) {
            val result = try {
                client.get("$ADDRESS/kanji/kanji_data").body()
            } catch (e: Exception) {
                e.printStackTrace()
                emptyList<Kanji>()
            }
            result.forEach { kanji ->
                getKanjiByIdCache[kanji.id] = kanji
            }
        }
    }

    suspend fun getLottieByKanjiId(kanjiId: String): String {
        getLottieByKanjiIdCache[kanjiId]?.let { return it }

        val result = try {
            client.get("$ADDRESS/lottie/id")
            { url { parameters.append("kanji_id", kanjiId) } }.body()
        } catch (e: Exception) {
            e.printStackTrace()
            Lottie()
        }
        getLottieByKanjiIdCache[kanjiId] = result.data
        return result.data
    }

    suspend fun loadAllLotties() {
        if(getLottieByKanjiIdCache.isEmpty()) {
            val result = try {
                client.get("$ADDRESS/lottie").body()
            } catch (e: Exception) {
                e.printStackTrace()
                emptyList<Lottie>()
            }
            result.forEach { lottie ->
                getLottieByKanjiIdCache[lottie.id] = lottie.data
            }
        }
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