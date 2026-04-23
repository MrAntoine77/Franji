package fr.mrantoine.franji.storage

import android.content.Context
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File


@Serializable
data class Vocab(
    val fr: String = "",
    val jp: String = "",
    val lecture: Pronunciation = Pronunciation(),
    val id: String = "",
    val kanji: List<String> = emptyList(),
    val similarity: List<String> = emptyList()
)

@Serializable
data class VocabCache(
    val vocabById: Map<String, Vocab>
)

object VocabStorage {
    private val data_file_path = "vocab.json"

    private var vocabByIdCache = mutableMapOf<String, Vocab>()

    fun getVocabById(
        context: Context,
        vocabId: String
    ): Vocab {
        vocabByIdCache[vocabId]?.let { return it }
        val jsonString = context.assets.open(data_file_path)
            .bufferedReader()
            .use { it.readText() }

        val json = Json {
            ignoreUnknownKeys = true
        }

        val list: List<Vocab> =
            json.decodeFromString(jsonString)

        val vocab = list.firstOrNull { it.id == vocabId } ?: Vocab()
        if(vocab != Vocab()) {
            vocabByIdCache[vocabId] = vocab
        }
        return vocab
    }


    fun loadAll(context: Context) {
        if(vocabByIdCache.isEmpty()) {
            val jsonString = context.assets.open(data_file_path)
                .bufferedReader()
                .use { it.readText() }

            val json = Json {
                ignoreUnknownKeys = true
            }

            val vocabs: List<Vocab>  = json.decodeFromString(jsonString)

            vocabs.forEach { vocab ->
                vocabByIdCache[vocab.id] = vocab
            }
        }
    }


    fun search(text: String): List<Vocab> {
        if (text.isBlank()) return emptyList()

        val query = text.lowercase()

        fun startsWithMatch(vocab: Vocab): Boolean {
            return vocab.fr.lowercase().startsWith(query) ||
                    vocab.jp.lowercase().startsWith(query) ||
                    vocab.lecture.kana.startsWith(query)
        }

        fun containsMatch(vocab: Vocab): Boolean {
            return vocab.fr.lowercase().contains(query) ||
                    vocab.jp.lowercase().contains(query) ||
                    vocab.lecture.romaji.lowercase().contains(query) ||
                    vocab.lecture.kana.contains(query)
        }


        fun exactMatch(vocab: Vocab, query: String): Boolean {
            return vocab.fr.equals(query, ignoreCase = true) ||
                    vocab.jp.equals(query, ignoreCase = true) ||
                    vocab.lecture.romaji.equals(query, ignoreCase = true) ||
                    vocab.lecture.kana.equals(query, ignoreCase = true)
        }


        return vocabByIdCache.values
            .filter { containsMatch(it) }
            .sortedWith(
                compareBy<Vocab> { !exactMatch(it, query) }
                    .thenBy { !startsWithMatch(it) }
            )
    }



    fun get4LinkedVocab(context: Context, vocabId: String, categoryPath: String): List<Vocab> {
        val categoryIds = CategoryStorage.getCategoryByPath(context, categoryPath).toMutableList().filter { it.startsWith("vocab") }
        val resultIds = mutableListOf<String>()

        fun addBlock(id: String) {
            val base = vocabByIdCache[id] ?: return
            resultIds += id
            resultIds += base.similarity.shuffled().take(3).filter { it !in resultIds }
        }

        addBlock(vocabId)

        repeat(4 - resultIds.size) {
            categoryIds
                .filter { it !in resultIds }
                .randomOrNull()
                ?.let { resultIds.add(it) }
        }

        return resultIds.mapNotNull { vocabByIdCache[it] }.shuffled()
    }



    private val cache_file_path = "vocab_cache.json"
    fun clearCache(context: Context) {
        vocabByIdCache.clear()

        val cacheFile = File(context.filesDir, cache_file_path)
        if (cacheFile.exists()) {
            cacheFile.delete()
        }
    }
    fun saveCache(context: Context) {
        val cacheFile = File(context.filesDir, cache_file_path)

        val serializableCache = VocabCache(
            vocabById = vocabByIdCache.toMap()
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

            vocabByIdCache.clear()
            vocabByIdCache.putAll(loadedCache.vocabById)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}