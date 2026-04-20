package fr.mrantoine.franji.storage

import android.content.Context
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File



@Serializable
data class UserCache(
    val levelByPath: Map<String, Level>
)


@Serializable
data class Level(
    val progress: Float = 0f,
    val lastPlay: Int = 0
)


object UserStorage {

    private var levelByPathCache = mutableMapOf<String, Level>()

    fun loadAll(context: Context) {

        if(levelByPathCache.isEmpty()) {
            val paths = CategoryStorage.getKeys(context)
            paths.forEach { path ->
                levelByPathCache[path] = Level(0f, 0)
            }
            print(levelByPathCache)
        }
    }

    fun getCurrentLevel(): Level? {
        if (levelByPathCache.isEmpty()) return null

        val twelveHoursMillis = 12 * 60 * 60 * 1000
        val now = System.currentTimeMillis()

        val eligible = levelByPathCache.values.firstOrNull { level ->
            level.progress < 1f && (now - level.lastPlay) > twelveHoursMillis
        }

        if (eligible != null) return eligible

        return levelByPathCache.values.minByOrNull { it.lastPlay }
    }

    fun updateLevel(path: String, progress: Float = 0f, lastPlay: Int = 0): Boolean {
        if (levelByPathCache.isEmpty()) {
            return false
        }
        else {
            levelByPathCache[path] = Level(progress = progress, lastPlay = lastPlay)
            return true
        }
    }


    private val cache_file_path = "user_cache.json"
    fun clearCache(context: Context) {
        levelByPathCache.clear()

        val cacheFile = File(context.filesDir, cache_file_path)
        if (cacheFile.exists()) {
            cacheFile.delete()
        }
    }
    fun saveCache(context: Context) {
        val cacheFile = File(context.filesDir, cache_file_path)

        val serializableCache = UserCache(
            levelByPath = levelByPathCache.toMap()
        )

        val jsonString = Json.encodeToString(serializableCache)
        cacheFile.writeText(jsonString)
    }
    fun loadCache(context: Context) {
        val cacheFile = File(context.filesDir, cache_file_path)
        if (!cacheFile.exists()) return

        try {
            val jsonString = cacheFile.readText()
            val loadedCache = Json.decodeFromString<UserCache>(jsonString)

            levelByPathCache.clear()
            levelByPathCache.putAll(loadedCache.levelByPath)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}