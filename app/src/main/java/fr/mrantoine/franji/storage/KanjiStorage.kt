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
    private val kanji_data_file_path = "kanji.json"
    private val kana_data_file_path = "kana.json"
    private val lottie_data_file_path = "lottie.json"



    // ======================== KANJIS ============================

    private var kanjiByIdCache = mutableMapOf<String, Kanji>()
    fun getKanjiById(
        context: Context,
        kanjiId: String
    ): Kanji {
        kanjiByIdCache[kanjiId]?.let { return it }
        val jsonString = context.assets.open(kanji_data_file_path)
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
            val jsonString = context.assets.open(kanji_data_file_path)
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


    // ======================== KANAS ============================
    private var kanaByIdCache = mutableMapOf<String, Kana>()

    fun getKanaById(
        context: Context,
        kanaId: String
    ): Kana {
        kanaByIdCache[kanaId]?.let { return it }

        val jsonString = context.assets.open(kana_data_file_path)
            .bufferedReader()
            .use { it.readText() }

        val json = Json {
            ignoreUnknownKeys = true
        }

        val list: List<Kana> =
            json.decodeFromString(jsonString)

        val kana = list.firstOrNull { it.main_id == kanaId } ?: Kana()

        if (kana != Kana()) {
            kanaByIdCache[kanaId] = kana
        }

        return kana
    }


    fun loadAllKana(context: Context) {
        if (kanaByIdCache.isEmpty()) {

            val jsonString = context.assets.open(kana_data_file_path)
                .bufferedReader()
                .use { it.readText() }

            val json = Json {
                ignoreUnknownKeys = true
            }

            val kanaList: List<Kana> =
                json.decodeFromString(jsonString)

            kanaList.forEach { kana ->
                kanaByIdCache[kana.main_id] = kana
            }
        }
    }


    fun loadAll(context: Context) {
        loadAllKanji(context)
        loadAllKana(context)
        loadAllLotties(context)
    }


    // ======================== LOTTIES ============================
    private var lottieByIdCache = mutableMapOf<String, String>()

    fun getLottieById(
        context: Context,
        kanjiId: String
    ): String {
        lottieByIdCache[kanjiId]?.let { return it }
        val jsonString = context.assets.open(lottie_data_file_path)
            .bufferedReader()
            .use { it.readText() }

        val json = Json {
            ignoreUnknownKeys = true
        }

        val list: List<Lottie> =
            json.decodeFromString(jsonString)

        val lottie = list.firstOrNull { it.id == kanjiId }?.data ?: "{}"
        if(lottie != "{}") {
            lottieByIdCache[kanjiId] = lottie
        }
        return lottie
    }

    fun loadAllLotties(context: Context) {
        if (lottieByIdCache.isEmpty()) {

            val jsonString = context.assets.open(lottie_data_file_path)
                .bufferedReader()
                .use { it.readText() }

            val json = Json {
                ignoreUnknownKeys = true
            }

            val lottieList: List<Lottie> =
                json.decodeFromString(jsonString)

            lottieList.forEach { lottie ->
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