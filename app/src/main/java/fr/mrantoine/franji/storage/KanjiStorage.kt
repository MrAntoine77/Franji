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
data class Kana(
    val main_id: String = "",
    val lectures: List<KanaElement> = emptyList(),
)

@Serializable
data class KanaElement(
    val id: String = "",
    val fr: String = "",
    val jp: String = "",
    val angles: List<Float> = emptyList(),
)



@Serializable
data class Lottie(
    val id: String = "",
    val data: String = "{}"
)

@Serializable
data class KanjiCache(
    val kanjiById: Map<String, Kanji>,
    val kanaById: Map<String, Kana>,
    val lottieById: Map<String, String>
)

object KanjiStorage {
    private var kanjiByIdCache = mutableMapOf<String, Kanji>()
    private var kanaByIdCache = mutableMapOf<String, Kana>()

    private var lottieByIdCache = mutableMapOf<String, String>()

    // ======================== KANJIS ============================
    suspend fun getKanjiById(kanjiId: String): Kanji {
        kanjiByIdCache[kanjiId]?.let { return it }
        val result = try {
            client.get("$ADDRESS/kanji/id") {
                url { parameters.append("kanji_id", kanjiId) }
            }.body()
        } catch (e: Exception) {
            e.printStackTrace()
            Kanji()
        }
        kanjiByIdCache[kanjiId] = result
        return result
    }

    suspend fun loadAllKanji() {
        if(kanjiByIdCache.isEmpty()) {
            val result = try {
                client.get("$ADDRESS/kanji").body()
            } catch (e: Exception) {
                e.printStackTrace()
                emptyList<Kanji>()
            }
            result.forEach { kanji ->
                kanjiByIdCache[kanji.id] = kanji
            }
        }
    }


    // ======================== KANAS ============================

    suspend fun getKanaById(kanaId: String): Kana {
        kanaByIdCache[kanaId]?.let { return it }
        val result = try {
            client.get("$ADDRESS/kana/id") {
                url { parameters.append("kana_id", kanaId) }
            }.body()
        } catch (e: Exception) {
            e.printStackTrace()
            Kana()
        }
        kanaByIdCache[kanaId] = result
        return result
    }


    suspend fun loadAllKana() {
        if(kanaByIdCache.isEmpty()) {
            val result = try {
                client.get("$ADDRESS/kana").body()
            } catch (e: Exception) {
                e.printStackTrace()
                emptyList<Kana>()
            }
            result.forEach { kana ->
                kanaByIdCache[kana.main_id] = kana
            }
        }
    }


    // ======================== LOTTIES ============================

    suspend fun getLottieById(kanjiId: String): String {
        lottieByIdCache[kanjiId]?.let { return it }

        val result = try {
            client.get("$ADDRESS/lottie/id")
            { url { parameters.append("kanji_id", kanjiId) } }.body()
        } catch (e: Exception) {
            e.printStackTrace()
            Lottie()
        }
        lottieByIdCache[kanjiId] = result.data
        return result.data
    }

    suspend fun loadAllLotties() {
        if(lottieByIdCache.isEmpty()) {
            val result = try {
                client.get("$ADDRESS/lottie").body()
            } catch (e: Exception) {
                e.printStackTrace()
                emptyList<Lottie>()
            }
            result.forEach { lottie ->
                lottieByIdCache[lottie.id] = lottie.data
            }
        }
    }


    private val cache_file_path = "kanji_cache.json"
    fun clearCache(context: Context) {
        kanjiByIdCache.clear()
        lottieByIdCache.clear()

        val cacheFile = File(context.filesDir, cache_file_path)
        if (cacheFile.exists()) {
            cacheFile.delete()
        }
    }
    fun saveCache(context: Context) {
        val cacheFile = File(context.filesDir, cache_file_path)

        val serializableCache = KanjiCache(
            kanjiById = kanjiByIdCache.toMap(),
            kanaById = kanaByIdCache.toMap(),
            lottieById = lottieByIdCache.toMap()
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

            kanaByIdCache.clear()
            kanaByIdCache.putAll(loadedCache.kanaById)

            lottieByIdCache.clear()
            lottieByIdCache.putAll(loadedCache.lottieById)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}