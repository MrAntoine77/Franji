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
data class Vocab(
    val fr: String = "",
    val jp: String = "",
    val lecture: String = "",
    val id: String = ""
)

@Serializable
data class VocabCache(
    val vocabById: Map<String, Vocab>
)

object VocabStorage {
    private var getVocabByIdCache = mutableMapOf<String, Vocab>()


    suspend fun getVocabById(vocabId: String): Vocab {
        getVocabByIdCache[vocabId]?.let { return it }
        val result = try {
            client.get("http://$IP_ADDRESS:$PORT/vocab/id") {
                url { parameters.append("vocab_id", vocabId) }
            }.body()
        } catch (e: Exception) {
            e.printStackTrace()
            Vocab()
        }
        getVocabByIdCache[vocabId] = result
        return result
    }

    private val cache_file_path = "vocab_cache.json"
    fun clearCache(context: Context) {
        getVocabByIdCache.clear()

        val cacheFile = File(context.filesDir, cache_file_path)
        if (cacheFile.exists()) {
            cacheFile.delete()
        }
    }
    fun saveCache(context: Context) {
        val cacheFile = File(context.filesDir, cache_file_path)

        val serializableCache = VocabCache(
            vocabById = getVocabByIdCache.toMap()
        )

        val jsonString = Json.encodeToString(serializableCache)
        cacheFile.writeText(jsonString)
    }
    fun loadCache(context: Context) {
        val cacheFile = File(context.filesDir, cache_file_path)
        if (!cacheFile.exists()) return

        try {
            val jsonString = cacheFile.readText()
            val loadedCache = Json.decodeFromString<VocabCache>(jsonString)

            getVocabByIdCache.clear()
            getVocabByIdCache.putAll(loadedCache.vocabById)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}