package fr.mrantoine.franji.storage

import android.content.Context
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File


@Serializable
data class Lottie(
    val id: String = "",
    val data: String = "{}"
)

@Serializable
data class LottieCache(
    val lottieById: Map<String, String>
)

object LottieStorage {
    private val data_file_path = "lottie.json"

    private var lottieByIdCache = mutableMapOf<String, String>()

    fun getLottieById(
        context: Context,
        kanjiId: String
    ): String {
        lottieByIdCache[kanjiId]?.let { return it }
        val jsonString = context.assets.open(data_file_path)
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

            val jsonString = context.assets.open(data_file_path)
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

    fun loadAll(context: Context) {
        loadAllLotties(context)
    }


    private val cache_file_path = "lottie_cache.json"
    fun clearCache(context: Context) {
        lottieByIdCache.clear()

        val cacheFile = File(context.filesDir, cache_file_path)
        if (cacheFile.exists()) {
            cacheFile.delete()
        }
    }
    fun saveCache(context: Context) {
        val cacheFile = File(context.filesDir, cache_file_path)

        val serializableCache = LottieCache(
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
            val loadedCache = Json.decodeFromString<LottieCache>(jsonString)

            lottieByIdCache.clear()
            lottieByIdCache.putAll(loadedCache.lottieById)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}