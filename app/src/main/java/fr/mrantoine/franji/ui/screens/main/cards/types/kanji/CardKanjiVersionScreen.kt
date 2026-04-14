package fr.mrantoine.franji.ui.screens.main.cards.types.kanji

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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.mrantoine.franji.storage.Kanji
import fr.mrantoine.franji.storage.KanjiStorage
import fr.mrantoine.franji.storage.SettingsStorage
import fr.mrantoine.franji.storage.TtsStorage
import fr.mrantoine.franji.ui.components.CardTypeTag
import fr.mrantoine.franji.ui.components.ClickableAnimatedText
import fr.mrantoine.franji.ui.components.Lecture
import fr.mrantoine.franji.ui.components.Lottie
import fr.mrantoine.franji.ui.theme.Dimens

@Composable
fun CardKanjiVersionScreen(
    cardId: String,
    isRevealed: MutableState<Boolean>
) {

    var lottie by remember { mutableStateOf<String>("{}") }
    var kanji by remember { mutableStateOf(Kanji()) }
    val scrollState = rememberLazyListState()

    val hint = kanji.kanji
    val jp = if (SettingsStorage.isRomaji()) kanji.main_lecture.romaji else kanji.main_lecture.kana
    val fr: String =  kanji.main_lecture.fr

    LaunchedEffect(cardId) {
        lottie = KanjiStorage.getLottieByKanjiId(cardId)
        kanji = KanjiStorage.getKanjiById(cardId)
        scrollState.scrollToItem(0)
    }

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
    ClickableAnimatedText(
        text = hint,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth(),
        onClick = { TtsStorage.speak( jp) }
    )
    HorizontalDivider(
        color = MaterialTheme.colorScheme.secondary,
        thickness = 1.dp,
        modifier = Modifier.fillMaxWidth()
    )
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
                    ClickableAnimatedText(
                        text = jp,
                        fontSize = 32.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = Dimens.m),
                        textAlign = TextAlign.Center,
                        onClick = { TtsStorage.speak(jp) }
                    )
                    Text(
                        text = fr,
                        fontSize = 32.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = Dimens.l),
                        textAlign = TextAlign.Center
                    )

                }
            }
            item {
                Lecture(kanji.lectures)
            }

        }
    }
}

