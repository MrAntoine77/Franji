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


@Serializable
data class MainPage(
    val height: Int = 0,
    val width: Int = 0,
    val items: List<MainPageItem> = emptyList()
)

@Serializable
data class MainPageItem(
    val path: String = "",
    val img: String = "",
    val color: String = ""
)

@Serializable
data class MainPageCache(
    val page: Map<String, MainPage>,
)

object MainPageStorage {
    private var getMainPageCache = mutableMapOf<String, MainPage>()

    suspend fun getMainPage(path: String): MainPage {
        getMainPageCache[path]?.let { return it }

        val result = try {
            client.get("http://$IP_ADDRESS:$PORT/main_page/$path").body()
        } catch (e: Exception) {
            e.printStackTrace()
            MainPage()
        }

        getMainPageCache[path] = result
        return result
    }

    private val cache_file_path = "main_page.json"

    fun clearCache(context: Context) {
        getMainPageCache.clear()

        val cacheFile = File(context.filesDir, cache_file_path)
        if (cacheFile.exists()) {
            cacheFile.delete()
        }
    }


    fun saveCache(context: Context) {
        val cacheFile = File(context.filesDir, cache_file_path)

        val serializableCache = MainPageCache(
            page = getMainPageCache.toMap()
        )

        val jsonString = Json.encodeToString(serializableCache)
        cacheFile.writeText(jsonString)
    }

    fun loadCache(context: Context) {
        val cacheFile = File(context.filesDir, cache_file_path)
        if (!cacheFile.exists()) return

        try {
            val jsonString = cacheFile.readText()
            val loadedCache = Json.decodeFromString<MainPageCache>(jsonString)

            getMainPageCache.clear()
            getMainPageCache.putAll(loadedCache.page)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


}

