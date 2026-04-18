package fr.mrantoine.franji.ui.screens.main.cards.types.kana

import android.speech.tts.TextToSpeech
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.mrantoine.franji.storage.Kana
import fr.mrantoine.franji.storage.KanaElement
import fr.mrantoine.franji.storage.Kanji
import fr.mrantoine.franji.storage.KanjiStorage
import fr.mrantoine.franji.storage.SettingsStorage
import fr.mrantoine.franji.storage.TtsStorage
import fr.mrantoine.franji.ui.components.CardTypeTag
import fr.mrantoine.franji.ui.components.ClickableAnimatedText
import fr.mrantoine.franji.ui.components.DrawArea
import fr.mrantoine.franji.ui.components.Lecture
import fr.mrantoine.franji.ui.components.Lottie
import fr.mrantoine.franji.ui.theme.Dimens
import java.util.Locale

@Composable
fun CardKanaThemeScreen(
    cardId: String,
    isRevealed: MutableState<Boolean>
) {

    var lottie by remember { mutableStateOf("{}") }
    var kana by remember { mutableStateOf(Kana()) }
    val scrollState = rememberLazyListState()
    var kanaExample by remember { mutableStateOf(KanaElement()) }
    val context = LocalContext.current

    LaunchedEffect(cardId) {
        kana = KanjiStorage.getKanaById(context, cardId)
        kanaExample = kana.lectures[0]
        lottie = KanjiStorage.getLottieById(context, kanaExample.id)
        scrollState.scrollToItem(0)
    }

    val hint = kanaExample.fr
    val jp: String =  kanaExample.jp

    LaunchedEffect( isRevealed.value) {
        if (isRevealed.value) {
            TtsStorage.speak(jp)
        }
    }

    CardTypeTag(
        text = "Kanji",
        color = MaterialTheme.colorScheme.primary,
        textColor = MaterialTheme.colorScheme.onPrimary
    )
    Spacer(modifier = Modifier.height(Dimens.s))
    Text(
        text = hint,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth()
    )
    HorizontalDivider(
        color = MaterialTheme.colorScheme.secondary,
        thickness = 1.dp,
        modifier = Modifier.fillMaxWidth()
    )

    if(!isRevealed.value){
        DrawArea(kanaExample.angles, isRevealed, lottie)
    }
    if(isRevealed.value) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            state = scrollState,
        ) {

            item {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Lottie(
                        data = lottie,
                        speed = 2f
                    )
                }
            }
        }
    }
}

