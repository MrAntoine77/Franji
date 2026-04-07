package fr.mrantoine.franji.ui.screens.main.cards.types.kanji

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import fr.mrantoine.franji.ui.components.CardTypeTag
import fr.mrantoine.franji.ui.components.DrawArea
import fr.mrantoine.franji.ui.components.Lecture
import fr.mrantoine.franji.ui.components.Lottie
import fr.mrantoine.franji.ui.theme.Dimens

@Composable
fun CardKanjiThemeScreen(
    cardId: String,
    isRevealed: MutableState<Boolean>,
) {

    var lottie by remember { mutableStateOf<String>("{}") }
    var kanji by remember { mutableStateOf(Kanji()) }

    LaunchedEffect(!isRevealed.value) {
        lottie = KanjiStorage.getLottieByKanjiId(cardId)
    }
    LaunchedEffect(!isRevealed.value) {
        kanji = KanjiStorage.getKanjiById(cardId)
    }

    val hint = kanji.lectures.firstOrNull()?.let { lecture ->
        lecture.fr.firstOrNull()?.takeIf { it.isNotEmpty() } ?: ""
    } ?: ""

    CardTypeTag(
        text = "Kanji",
        color = MaterialTheme.colorScheme.primary,
        textColor = Color.White
    )
    Text(
        text = hint,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth()
    )
    HorizontalDivider(
        color = Color.Gray,
        thickness = 1.dp,
        modifier = Modifier.fillMaxWidth()
    )

    if(!isRevealed.value){
        DrawArea(kanji.angles, isRevealed, lottie)
    }
    val scrollState = rememberLazyListState()
    if(isRevealed.value) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            state = scrollState,
        ) {

            item {
                val translation = kanji.lectures.firstOrNull()?.let { lecture ->
                    lecture.kun.firstOrNull()?.takeIf { it.isNotEmpty() }
                        ?: lecture.ON.firstOrNull()?.takeIf { it.isNotEmpty() }
                        ?: ""
                } ?: ""
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = translation,
                        fontSize = 20.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = Dimens.l),
                        textAlign = TextAlign.Center
                    )
                    Lottie(
                        data = lottie,
                        speed = 2f
                    )
                }
            }
            items(kanji.lectures.size) { index ->
                Lecture(
                    french = kanji.lectures[index].fr,
                    ON = kanji.lectures[index].ON,
                    kun = kanji.lectures[index].kun
                )
            }
        }
    }
}

