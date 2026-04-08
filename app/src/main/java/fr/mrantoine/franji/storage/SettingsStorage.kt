package fr.mrantoine.franji.storage

import IP_ADDRESS
import PORT
import android.content.Context
import client

import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import kotlin.collections.mutableMapOf


@Serializable
data class SettingsCache(
    val isRomaji: Boolean = false,
)

object SettingsStorage {
    private val settings = SettingsCache()

    fun isRomaji(): Boolean {
        return settings.isRomaji
    }

    private val cache_file_path = "settings_cache.json"

    fun clearCache(context: Context) {

    }

    fun saveCache(context: Context) {

    }

    fun loadCache(context: Context) {

    }
}








