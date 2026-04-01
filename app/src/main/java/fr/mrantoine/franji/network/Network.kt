import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.bodyAsText
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive


var IP_ADDRESS = "192.168.1.182"
var PORT = "8000"

val client = HttpClient(CIO) {
    install(ContentNegotiation) {
        json(Json {
            ignoreUnknownKeys = true
        })
    }
}

suspend fun getCategory(path: String): Map<String, Any> {
    return try {
        val response = client.get("http://$IP_ADDRESS:$PORT/categories/$path")
        val jsonString: String = response.body()

        val jsonObject: JsonObject = Json.parseToJsonElement(jsonString).jsonObject
        jsonObject.mapValues { it.value }
    } catch (e: Exception) {
        e.printStackTrace()
        emptyMap()
    }
}

suspend fun getCategoryArray(path: String): Array<String> {
    return try {
        val response = client.get("http://$IP_ADDRESS:$PORT/categories/$path")
        val jsonString: String = response.body()

        val jsonArray: JsonArray = Json.parseToJsonElement(jsonString).jsonArray
        jsonArray.map { it.jsonPrimitive.content }.toTypedArray()
    } catch (e: Exception) {
        e.printStackTrace()
        emptyArray()
    }
}

suspend fun getLottie(kanji: String): String {
    return try {
        client.get("http://$IP_ADDRESS:$PORT/lottie/$kanji")
            .body<String>()
            .trimIndent()
    } catch (e: Exception) {
        e.printStackTrace()
        "{}"
    }
}

@Serializable
data class Lecture(
    val fr: List<String> = emptyList(),
    val ON: List<String> = emptyList(),
    val kun: List<String> = emptyList()
)


@Serializable
data class Kanji(
    val kanji: String,
    val lectures: List<Lecture> = emptyList(),
    val id: String
)

suspend fun getKanjiByCharId(charId: String): Kanji {
    return try {
        client.get("http://$IP_ADDRESS:$PORT/kanji/char_id/$charId").body()
    } catch (e: Exception) {
        e.printStackTrace()
        Kanji("", emptyList(), "")
    }
}