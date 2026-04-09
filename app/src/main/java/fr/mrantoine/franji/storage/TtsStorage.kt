package fr.mrantoine.franji.storage

import android.content.Context
import android.speech.tts.TextToSpeech
import androidx.compose.runtime.mutableStateOf
import kotlinx.serialization.Serializable
import java.util.Locale

object TtsStorage {

    private var tts: TextToSpeech? = null
    private var isReady = false
    private var pendingText: String? = null
    private var pendingId: String? = null

    fun init(context: Context) {
        if (tts != null) return  // déjà initialisé

        tts = TextToSpeech(context.applicationContext) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts?.language = Locale.JAPANESE
                isReady = true

                // Si du texte attendait avant initialisation
                if (pendingText != null) {
                    tts?.speak(pendingText, TextToSpeech.QUEUE_FLUSH, null, pendingId)
                    pendingText = null
                    pendingId = null
                }
            }
        }
    }

    fun speak(text: String, id: String = "tts") {
        val charsToRemove = setOf('(', ')', '[', ']', '-')
        val filteredText = text.filterNot { it in charsToRemove }

        if (isReady) {
            tts?.speak(filteredText, TextToSpeech.QUEUE_FLUSH, null, id)
        } else {
            pendingText = filteredText
            pendingId = id
        }
    }

    fun shutdown() {
        tts?.shutdown()
        tts = null
        isReady = false
    }
}