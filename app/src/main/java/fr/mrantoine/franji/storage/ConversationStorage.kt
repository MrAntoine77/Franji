package fr.mrantoine.franji.storage

import android.content.Context
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import kotlin.collections.mutableMapOf

@Serializable
data class ConversationCache(
    val conversations: Map<String, Conversation>,
)

@Serializable
data class Conversation(
    val objective: String,
    val parts: List<ConversationPart>,
    val phases: List<List<String>>
)

@Serializable
data class ConversationPart(
    val name: String,
    val elements: List<ConversationElement>
)

@Serializable
data class ConversationElement(
    val fr: String,
    val jp: String
)


object ConversationStorage {
    private val data_file_path = "conversations.json"

    private val conversationCache = mutableMapOf<String, Conversation>()




    fun loadAll(context: Context) {
        if (conversationCache.isEmpty()) {

            val jsonString = context.assets.open(data_file_path)
                .bufferedReader()
                .use { it.readText() }

            val json = Json {
                ignoreUnknownKeys = true
            }

            val map: Map<String, Conversation> =
                json.decodeFromString(jsonString)

            map.forEach { (key, conversation) ->
                conversationCache[key] = conversation
            }
        }
    }

    private val cache_file_path = "conversation_cache.json"

    fun clearCache(context: Context) {
        conversationCache.clear()

        val cacheFile = File(context.filesDir, cache_file_path)
        if (cacheFile.exists()) {
            cacheFile.delete()
        }
    }

    fun saveCache(context: Context) {
        val cacheFile = File(context.filesDir, cache_file_path)

        val serializableCache = ConversationCache(
            conversations = conversationCache.toMap()
        )

        val jsonString = Json.encodeToString(serializableCache)
        cacheFile.writeText(jsonString)
    }

    fun loadCache(context: Context) {
        val cacheFile = File(context.filesDir, cache_file_path)
        if (!cacheFile.exists()) return

        try {
            val jsonString = cacheFile.readText()
            val loadedCache = Json.decodeFromString<ConversationCache>(jsonString)

            conversationCache.clear()
            conversationCache.putAll(loadedCache.conversations)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}








