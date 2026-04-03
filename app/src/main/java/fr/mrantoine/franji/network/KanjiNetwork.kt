package fr.mrantoine.franji.network

import IP_ADDRESS
import PORT
import client
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.serialization.Serializable

@Serializable
data class Lecture(
    val fr: List<String> = emptyList(),
    val ON: List<String> = emptyList(),
    val kun: List<String> = emptyList()
)


@Serializable
data class Kanji(
    val kanji: String = "",
    val lectures: List<Lecture> = emptyList(),
    val id: String = ""
)

suspend fun getKanjiById(kanji_id: String): Kanji {
    return try {
        client.get("http://$IP_ADDRESS:$PORT/kanji/kanji_data/id/$kanji_id").body()
    } catch (e: Exception) {
        e.printStackTrace()
        Kanji("", emptyList(), "")
    }
}

suspend fun getKanjiByChar(kanji_char: String): Kanji {
    return try {
        client.get("http://$IP_ADDRESS:$PORT/kanji/kanji_data/kanji") {
            url {
                parameters.append("kanji_char", kanji_char)
            }
        }.body()
    } catch (e: Exception) {
        e.printStackTrace()
        Kanji("", emptyList(), "")
    }
}