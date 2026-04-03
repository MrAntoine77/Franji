package fr.mrantoine.franji.storage

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


object KanjiStorage {
    private var getKanjiByIdCache = mutableMapOf<String, Kanji>()

    suspend fun getKanjiById(kanji_id: String): Kanji {
        getKanjiByIdCache[kanji_id]?.let {
            return it
        }


        val result =  try {
            client.get("http://$IP_ADDRESS:$PORT/kanji/kanji_data/id/$kanji_id").body()
        } catch (e: Exception) {
            e.printStackTrace()
            Kanji("", emptyList(), "")
        }
        getKanjiByIdCache[kanji_id] = result
        return result
    }


    private val getKanjiByCharCache = mutableMapOf<String, Kanji>()
    suspend fun getKanjiByChar(kanji_char: String): Kanji {
        getKanjiByCharCache[kanji_char]?.let {
            return it
        }

        val result =  try {
            client.get("http://$IP_ADDRESS:$PORT/kanji/kanji_data/kanji") {
                url {
                    parameters.append("kanji_char", kanji_char)
                }
            }.body()
        } catch (e: Exception) {
            e.printStackTrace()
            Kanji("", emptyList(), "")
        }

        getKanjiByCharCache[kanji_char] = result
        return result
    }

    private val getLottieByKanjiIdCache = mutableMapOf<String, String>()
    suspend fun getLottieByKanjiId(kanji_id: String): String {
        getLottieByKanjiIdCache[kanji_id]?.let {
            return it
        }

        val result = try {
            client.get("http://$IP_ADDRESS:$PORT/lottie/$kanji_id")
                .body<String>()
                .trimIndent()
        } catch (e: Exception) {
            e.printStackTrace()
            "{}"
        }
        getLottieByKanjiIdCache[kanji_id] = result
        return result
    }
}



