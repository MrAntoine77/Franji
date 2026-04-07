package fr.mrantoine.franji.ui.screens.main.cards.types

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
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
import fr.mrantoine.franji.storage.Vocab
import fr.mrantoine.franji.storage.VocabStorage
import fr.mrantoine.franji.ui.theme.Dimens

@Composable
fun CardVocabThemeScreen(
    cardId: String,
    isRevealed: MutableState<Boolean>,
) {

    var vocab by remember { mutableStateOf(Vocab()) }

    LaunchedEffect(isRevealed.value == false) {
        vocab = VocabStorage.getVocabById(cardId)
    }
    val hint = vocab.fr

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = hint,
            fontSize = 32.sp
        )
    }
    HorizontalDivider(
        color = Color.Gray,
        thickness = 1.dp,
        modifier = Modifier.fillMaxWidth()
    )

    val scrollState = rememberLazyListState()
    if(isRevealed.value) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            state = scrollState,
        ) {

            item {
                val translation = vocab.jp
                val lecture = vocab.lecture
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = translation,
                        fontSize = 48.sp,
                        lineHeight = 56.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = Dimens.l),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = lecture,
                        fontSize = 20.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = Dimens.l),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

