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
    val lecture: Pronunciation = Pronunciation(),
    val elements: List<String> = emptyList()
)

@Serializable
data class GrammarCache(
    val grammarById: Map<String, Grammar>
)

object GrammarStorage {
    private val data_file_path = "grammar.json"

    private var grammarByIdCache = mutableMapOf<String, Grammar>()
    fun getGrammarById(
        context: Context,
        grammarId: String
    ): Grammar {
        grammarByIdCache[grammarId]?.let { return it }
        val jsonString = context.assets.open(data_file_path)
            .bufferedReader()
            .use { it.readText() }

        val json = Json {
            ignoreUnknownKeys = true
        }

        val list: List<Grammar> =
            json.decodeFromString(jsonString)

        val grammar = list.firstOrNull { it.id == grammarId } ?: Grammar()
        if(grammar != Grammar()) {
            grammarByIdCache[grammarId] = grammar
        }
        return grammar
    }


    fun loadAll(context: Context) {
        if(grammarByIdCache.isEmpty()) {
            val jsonString = context.assets.open(data_file_path)
                .bufferedReader()
                .use { it.readText() }

            val json = Json {
                ignoreUnknownKeys = true
            }

            val grammars: List<Grammar>  = json.decodeFromString(jsonString)

            grammars.forEach { grammar ->
                grammarByIdCache[grammar.id] = grammar
            }
        }
    }

    fun search(text: String): List<Grammar> {
        if (text.isBlank()) return emptyList()

        val query = text.lowercase()

        fun filterSpecial(text: String): String {
            return text.filterNot { it in setOf('(', ')', '.', '-', '~', '～') }
        }


        fun startsWithMatch(grammar: Grammar): Boolean {
            return grammar.title.lowercase().startsWith(query) ||
                    grammar.subtitle.lowercase().startsWith(query) ||
                    grammar.desc.lowercase().startsWith(query)
        }

        fun containsMatch(grammar: Grammar): Boolean {
            return grammar.title.lowercase().contains(query) ||
                    grammar.subtitle.lowercase().contains(query) ||
                    grammar.desc.lowercase().contains(query)
        }


        fun exactMatch(grammar: Grammar, query: String): Boolean {
            val cleanQuery = filterSpecial(query).lowercase()

            fun words(text: String) =
                filterSpecial(text)
                    .split(" ")
                    .filter { it.isNotBlank() }
                    .map { it.lowercase() }

            return words(grammar.title).any { it == cleanQuery } ||
                    words(grammar.subtitle).any { it == cleanQuery }
        }

        return grammarByIdCache.values
            .filter { containsMatch(it) }
            .sortedWith(
                compareBy<Grammar> { !exactMatch(it, query) }
                    .thenBy { !startsWithMatch(it) }
            )
    }


    private val cache_file_path = "grammar_cache.json"
    fun clearCache(context: Context) {
        grammarByIdCache.clear()

        val cacheFile = File(context.filesDir, cache_file_path)
        if (cacheFile.exists()) {
            cacheFile.delete()
        }
    }
    fun saveCache(context: Context) {
        val cacheFile = File(context.filesDir, cache_file_path)

        val serializableCache = GrammarCache(
            grammarById = grammarByIdCache.toMap()
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

            grammarByIdCache.clear()
            grammarByIdCache.putAll(loadedCache.grammarById)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}