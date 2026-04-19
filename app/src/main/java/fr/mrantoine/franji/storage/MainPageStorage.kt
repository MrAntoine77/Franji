package fr.mrantoine.franji.storage

import android.content.Context
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
    val mainPage: Map<String, MainPage>,
)

object MainPageStorage {
    private val data_file_path = "main_page.json"
    private var mainPageCache = mutableMapOf<String, MainPage>()


    fun getMainPage(context: Context, path: String): MainPage {
        mainPageCache[path]?.let { return it }

        val jsonString = context.assets.open(data_file_path)
            .bufferedReader()
            .use { it.readText() }

        val json = Json {
            ignoreUnknownKeys = true
        }

        val map: Map<String, MainPage> =
            json.decodeFromString(jsonString)

        val page = map[path] ?: MainPage()

        if (page != MainPage()) {
            mainPageCache[path] = page
        }

        return page
    }


    fun loadAll(context: Context) {
        if (mainPageCache.isEmpty()) {

            val jsonString = context.assets.open(data_file_path)
                .bufferedReader()
                .use { it.readText() }

            val json = Json {
                ignoreUnknownKeys = true
            }

            val map: Map<String, MainPage> =
                json.decodeFromString(jsonString)

            map.forEach { (key, page) ->
                mainPageCache[key] = page
            }
        }
    }


    private val cache_file_path = "main_page_cache.json"

    fun clearCache(context: Context) {
        mainPageCache.clear()

        val cacheFile = File(context.filesDir, cache_file_path)
        if (cacheFile.exists()) {
            cacheFile.delete()
        }
    }


    fun saveCache(context: Context) {
        val cacheFile = File(context.filesDir, cache_file_path)

        val serializableCache = MainPageCache(
            mainPage = mainPageCache.toMap()
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

            mainPageCache.clear()
            mainPageCache.putAll(loadedCache.mainPage)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

