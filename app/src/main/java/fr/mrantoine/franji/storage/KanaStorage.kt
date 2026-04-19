package fr.mrantoine.franji.storage

import android.content.Context
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File



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
data class KanaCache(
    val kanaById: Map<String, Kana>,
)

object KanaStorage {
    private val data_file_path = "kana.json"

    private var kanaByIdCache = mutableMapOf<String, Kana>()

    fun getKanaById(
        context: Context,
        kanaId: String
    ): Kana {
        kanaByIdCache[kanaId]?.let { return it }

        val jsonString = context.assets.open(data_file_path)
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

            val jsonString = context.assets.open(data_file_path)
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
        loadAllKana(context)
    }

    private val cache_file_path = "kana_cache.json"
    fun clearCache(context: Context) {
        kanaByIdCache.clear()

        val cacheFile = File(context.filesDir, cache_file_path)
        if (cacheFile.exists()) {
            cacheFile.delete()
        }
    }
    fun saveCache(context: Context) {
        val cacheFile = File(context.filesDir, cache_file_path)

        val serializableCache = KanaCache(
            kanaById = kanaByIdCache.toMap()
        )

        val jsonString = Json.encodeToString(serializableCache)
        cacheFile.writeText(jsonString)
    }
    fun loadCache(context: Context) {
        val cacheFile = File(context.filesDir, cache_file_path)
        if (!cacheFile.exists()) return

        try {
            val jsonString = cacheFile.readText()
            val loadedCache = Json.decodeFromString<KanaCache>(jsonString)

            kanaByIdCache.clear()
            kanaByIdCache.putAll(loadedCache.kanaById)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}