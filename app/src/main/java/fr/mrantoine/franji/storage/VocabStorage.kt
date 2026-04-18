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
    val kanji: List<String> = emptyList()
)

@Serializable
data class VocabCache(
    val vocabById: Map<String, Vocab>
)

object VocabStorage {
    private var vocabByIdCache = mutableMapOf<String, Vocab>()

    fun getVocabById(
        context: Context,
        vocabId: String
    ): Vocab {
        vocabByIdCache[vocabId]?.let { return it }
        val jsonString = context.assets.open("vocab.json")
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
            val jsonString = context.assets.open("vocab.json")
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