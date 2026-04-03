package fr.mrantoine.franji.network

import IP_ADDRESS
import PORT
import client
import io.ktor.client.call.body
import io.ktor.client.request.get

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