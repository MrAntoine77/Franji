package fr.mrantoine.franji.network

import IP_ADDRESS
import PORT
import client
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject


@Serializable
data class MainPage(
    val Kanji: MainPageItem = MainPageItem(),
    val Kana: MainPageItem = MainPageItem(),
    val Vocabulaire: MainPageItem = MainPageItem(),
    val Grammaire: MainPageItem = MainPageItem()
)

@Serializable
data class MainPageItem(
    val height: Int = 0,
    val width: Int = 0,
    val paths: List<String> = emptyList()
)

suspend fun getMainMage(): MainPage {
    return try {
        client.get("http://$IP_ADDRESS:$PORT/main_page").body()
    } catch (e: Exception) {
        e.printStackTrace()
        MainPage()
    }
}