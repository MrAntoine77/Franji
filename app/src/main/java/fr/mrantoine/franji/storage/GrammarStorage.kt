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
data class Grammar(
    val id: String = "",
    val title: String = "",
    val desc : String = "",
    val examples: List<GrammarItem> = emptyList()
)

@Serializable
data class GrammarItem(
    val jp: String = "",
    val fr: String = "",
    val lecture: String = "",
    val highlighted: List<Int> = emptyList()
)

@Serializable
data class GrammarCache(
    val grammarById: Map<String, Grammar>
)

object GrammarStorage {
    private var getGrammarByIdCache = mutableMapOf<String, Grammar>()
    suspend fun getGrammarById(grammarId: String): Grammar {
        getGrammarByIdCache[grammarId]?.let { return it }
        val result = try {
            client.get("http://$IP_ADDRESS:$PORT/grammar/id") {
                url { parameters.append("grammar_id", grammarId) }
            }.body()
        } catch (e: Exception) {
            e.printStackTrace()
            Grammar()
        }
        getGrammarByIdCache[grammarId] = result
        return result
    }

    private val cache_file_path = "grammar_cache.json"
    fun clearCache(context: Context) {
        getGrammarByIdCache.clear()

        val cacheFile = File(context.filesDir, cache_file_path)
        if (cacheFile.exists()) {
            cacheFile.delete()
        }
    }
    fun saveCache(context: Context) {
        val cacheFile = File(context.filesDir, cache_file_path)

        val serializableCache = GrammarCache(
            grammarById = getGrammarByIdCache.toMap()
        )

        val jsonString = Json.encodeToString(serializableCache)
        cacheFile.writeText(jsonString)
    }
    fun loadCache(context: Context) {
        val cacheFile = File(context.filesDir, cache_file_path)
        if (!cacheFile.exists()) return

        try {
            val jsonString = cacheFile.readText()
            val loadedCache = Json.decodeFromString<GrammarCache>(jsonString)

            getGrammarByIdCache.clear()
            getGrammarByIdCache.putAll(loadedCache.grammarById)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}