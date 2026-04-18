package fr.mrantoine.franji.storage

import android.content.Context
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File


@Serializable
data class Grammar(
    val id: String = "",
    val title: String = "",
    val subtitle: String = "",
    val desc : String = "",
    val examples: List<GrammarItem> = emptyList()
)

@Serializable
data class GrammarItem(
    val jp: String = "",
    val fr: String = "",
    val lecture: Pronunciation = Pronunciation()
)

@Serializable
data class GrammarCache(
    val grammarById: Map<String, Grammar>
)

object GrammarStorage {
    private var getGrammarByIdCache = mutableMapOf<String, Grammar>()
    fun getGrammarById(
        context: Context,
        grammarId: String
    ): Grammar {
        getGrammarByIdCache[grammarId]?.let { return it }
        val jsonString = context.assets.open("grammar.json")
            .bufferedReader()
            .use { it.readText() }

        val json = Json {
            ignoreUnknownKeys = true
        }

        val list: List<Grammar> =
            json.decodeFromString(jsonString)

        val grammar = list.firstOrNull { it.id == grammarId } ?: Grammar()
        if(grammar != Grammar()) {
            getGrammarByIdCache[grammarId] = grammar
        }
        return grammar
    }


    fun loadAll(context: Context) {
        if(getGrammarByIdCache.isEmpty()) {
            val jsonString = context.assets.open("grammar.json")
                .bufferedReader()
                .use { it.readText() }

            val json = Json {
                ignoreUnknownKeys = true
            }

            val grammars: List<Grammar>  = json.decodeFromString(jsonString)

            grammars.forEach { grammar ->
                getGrammarByIdCache[grammar.id] = grammar
            }
        }
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