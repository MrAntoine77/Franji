package fr.mrantoine.franji.storage

import android.content.Context
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.File


@Serializable
data class SettingsCache(
    var isRomaji: Boolean = false,
    var restCacheOnLaunch: Boolean = false,
)

object SettingsStorage {
    private var settings = SettingsCache()

    fun isRomaji(): Boolean {
        return settings.isRomaji
    }

    fun setRomaji(isRomaji: Boolean) {
        settings.isRomaji = isRomaji
    }

    fun isRestCacheOnLaunch(): Boolean {
        return settings.restCacheOnLaunch
    }

    fun setRestCacheOnLaunch(reset: Boolean) {
        settings.restCacheOnLaunch = reset
    }

    private val cache_file_path = "settings_cache.json"

    fun clearCache(context: Context) {
        val cacheFile = File(context.filesDir, cache_file_path)
        if (cacheFile.exists()) {
            cacheFile.delete()
        }
    }

    fun saveCache(context: Context) {
        val file = File(context.filesDir, cache_file_path)
        val jsonText = Json.encodeToString(SettingsCache.serializer(), settings)
        file.writeText(jsonText)
    }

    fun loadCache(context: Context) {
        val file = File(context.filesDir, cache_file_path)
        if (!file.exists()) {
            settings = SettingsCache()
            return
        }
        val jsonText = file.readText()

        try {
            settings = Json.decodeFromString(SettingsCache.serializer(), jsonText)
        } catch (e: Exception) {
            settings = SettingsCache()
        }
    }
}








